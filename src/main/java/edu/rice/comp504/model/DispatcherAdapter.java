package edu.rice.comp504.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import edu.rice.comp504.model.cmd.*;
import edu.rice.comp504.model.obj.ChatBox;
import org.eclipse.jetty.websocket.api.Session;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.Message;
import edu.rice.comp504.model.obj.User;
import edu.rice.comp504.model.res.*;

public class DispatcherAdapter extends Observable {

    private AtomicInteger nextUserId;
    private AtomicInteger nextRoomId;
    private AtomicInteger nextMessageId;

    // Maps user id to the user
    private Map<Integer, User> users;

    // Maps room id to the chat room
    private Map<Integer, ChatRoom> rooms;

    // Maps message id to the message
    private Map<Integer, Message> messages;

    // Maps session to user id
    // By calling userIdFromSession.inverse(), we can get a map from userId to session which will be used by notifyClient(user, AResponse)
    private BiMap<Session, Integer> userIdFromSession;

    /**
     * Constructor, initializing all private fields.
     */
    public DispatcherAdapter() {
        this.nextRoomId = new AtomicInteger(0);
        this.nextUserId = new AtomicInteger(0);
        this.nextMessageId = new AtomicInteger(0);
        this.users = new ConcurrentHashMap<>();
        this.rooms = new ConcurrentHashMap<>();
        this.messages = new ConcurrentHashMap<>();
        this.userIdFromSession = Maps.synchronizedBiMap(HashBiMap.create());
    }

    /**
     * Allocate a user id for a new session.
     * @param session the new session
     */
    //TODO:   controller should call this method, and then call loadUser.  (-Alex)
    public void newSession(Session session) {
        advanceCounter(nextUserId.get());
        userIdFromSession.put(session, nextUserId.get());
    }

    /**
     * Separate method to apply operator ++, but within a synchronized method
     * @param counter
     */
    public synchronized void advanceCounter(int counter) {
        counter++;
    }


    /**
     * Get the user if from a session.
     * @param session the session
     * @return the user id binding with session
     */
    public int getUserIdFromSession(Session session) {
        return this.userIdFromSession.get(session);
    }

    /**
     * Determine whether the session exists.
     * @param session the session
     * @return whether the session is still connected or not
     */
    public boolean containsSession(Session session) {
        return this.userIdFromSession.containsKey(session);
    }

    /**
     * Load a user into the environment.
     * @param session the session that requests to called the method
     * @param body of format "name age location school"
     * @return the new user that has been loaded
     */
    public User loadUser(Session session, String body) {
        String[] tokens = body.split(" ");
        int my_id = getUserIdFromSession(session);
        User my_user = new User(my_id, session, tokens[0], Integer.valueOf(tokens[1]),
                tokens[2], tokens[3], null);
        users.put(my_id, my_user);

        for(ChatRoom room: rooms.values()) {
            if(room.applyFilter(my_user)) my_user.addRoom(room);
        }

        //TODO: call our two methods for sending room info and sending chat info

        return my_user;
    }


    /**
     * Load a room into the environment.
     * @param session the session that requests to called the method
     * @param body of format "name ageLower ageUpper {[location],}*{[location]} {[school],}*{[school]}"
     * @return the new room that has been loaded
     */
    public ChatRoom loadRoom(Session session, String body) {
        //get this user
        int user_id = getUserIdFromSession(session);
        User my_user = users.get(user_id);

        //check the specifications in the body
        String[] info = body.split(" ");
        Preconditions.checkArgument(info.length == 6 && info[0].equals("create"), "Illegal create room message format: %s", body);
        String roomName = info[1];
        int ageLower = Integer.parseInt(info[2]);
        int ageUpper = Integer.parseInt(info[3]);

        //construct the room
        ChatRoom my_room = new ChatRoom(nextRoomId.getAndIncrement(), roomName, my_user, ageLower, ageUpper, info[4].split(","), info[5].split(","), this);

        if(!my_room.applyFilter(my_user)) {

            return null;

        } else {

            rooms.put(nextRoomId.get(), my_room);

            //update user's join list
            my_user.addRoom(my_room);
            my_user.moveToJoined(my_room);

            IUserCmd cmd = CmdFactory.makeAddRoomCmd(my_room);
            notifyObservers(cmd);
            //TODO: send room info to all clients

            return my_room;
        }
    }

    /**
     * Remove a user with given userId from the environment.
     * @param userId the id of the user to be removed
     */
    public void unloadUser(int userId) {

        //get this user
        User my_user = users.get(userId);
        Session my_session = my_user.getSession();
        List<Integer> joined_rooms = my_user.getJoinedRoomIds();

        //remove user from all rooms
        for(Integer room_id : joined_rooms) {
            rooms.get(room_id).addNotification(my_user.getName()+" is logging out.");
            leaveRoom(my_session, "leave "+room_id);
            // rooms.get(room_id).removeUser(my_user, "user logged out.")
        }

        //remove user from map
        users.remove(userId);

    }

    /**
     * Remove a room with given roomId from the environment.
     * @param roomId the id of the chat room to be removed
     */
    public void unloadRoom(int roomId) {
        rooms.get(roomId).removeAllUsers();

        //TODO: send message to all users in the room, if there is a way to do this somewhere not inside the
        // room window, which will be disappearing. This may be an action that occurs inside removeAllUsers()
        // in ChatRoom.java.

        //construct and send command to update joined/available lists of all users
        IUserCmd cmd = CmdFactory.makeRemoveRoomCmd(rooms.get(roomId));
        notifyObservers(cmd);

        //delete room from map.
        rooms.remove(roomId);
    }


    /**
     * Make a user join a chat room.
     * @param session the session that requests to called the method
     * @param body of format "roomId"
     */
    public void joinRoom(Session session, String body) {
        //get room from body
        String[] info = body.split(",");
        Preconditions.checkArgument(info.length == 2 && info[0].equals("join"), "Illegal join room message format: %s", body);
        int roomId = Integer.parseInt(info[1]);
        ChatRoom my_room = rooms.get(roomId);

        //get this user
        User my_user = users.get(getUserIdFromSession(session));

        //check room requirements, send rejection if join fails
        boolean join_okay = my_room.applyFilter(my_user);

        if(!join_okay) {

        } else {

            //update joined and available room lists for user
            my_user.moveToJoined(my_room);

            //add user as an observer of the room
            my_room.addUser(my_user);

        }
    }

    /**
     * Make a user volunteer to leave a chat room.
     * @param session the session that requests to called the method
     * @param body of format "roomId"
     */
    public void leaveRoom(Session session, String body) {
        //get room from body
        String[] info = body.split(" ");
        Preconditions.checkArgument(info.length == 2 && info[0].equals("join"), "Illegal join room message format: %s", body);
        int roomId = Integer.parseInt(info[1]);
        ChatRoom my_room = rooms.get(roomId);

        //get this user
        User my_user = users.get(getUserIdFromSession(session));

        //update joined/available rooms for this user
        my_user.moveToAvailable(my_room);

        //remove user as observer for this room, broadcast message to room
        my_room.removeUser(my_user, "user left voluntarily.");


        //delete room if this user is the owner
        if(my_room.getOwner() == my_user) unloadRoom(roomId);

    }

    public void voluntaryLeaveRoom(Session session, String body) {

        leaveRoom(session, body);
    }
    public void ejectFromRoom(Session session, String body){

        leaveRoom(session, body);
    }

    // TODO: I question the need for this method. We don't have to allow this. (-Alex)
    // TODO: Deprecated
    /**
     * Make modification on chat room filer by the owner.
     * @param session the session of the chat room owner
     * @param body of format "roomId lower upper {[location],}*{[location]} {[school],}*{[school]}"
     */
    public void modifyRoom(Session session, String body) {

    }

    /**
     * A sender sends a string message to a receiver.
     * @param session the session of the message sender
     * @param body of format "roomId receiverId rawMessage"
     */
    public void sendMessage(Session session, String body) {
        String[] info = body.split(" ");
        int messageId = nextMessageId.getAndIncrement();
        int roomId = Integer.parseInt(info[1]);
        int receiverId = Integer.parseInt(info[2]);
        int senderId = userIdFromSession.get(session);
        StringBuilder messageBuilder = new StringBuilder(info[3]);
        for (int i = 4;i < info.length;i++) {
            messageBuilder.append(" " + info[i]);
        }
        String message = messageBuilder.toString();

        // TODO: check if this message contain unallowed words
        if (Arrays.asList(message.split(" ")).contains("hate")) {
            ejectFromRoom(session, "leave " + roomId);
            return;
        }

        // Update entities in DA.
        Message newMsg = new Message(messageId, roomId, senderId, receiverId, message);
        messages.put(messageId, newMsg);
        rooms.get(roomId).storeMessage(users.get(senderId), users.get(receiverId), newMsg);

        try {
            users.get(receiverId).getSession().getRemote().sendString(getChatBoxForUser(receiverId).toJson());
        } catch (IOException excpetion) {
            System.out.println("Failed when sending message received confirmation!");
        }
    }

    /**
     * The owner of a room broadcast a message.
     *
     * body string format: "broadcast [roomId] [message]"
     */
    public void broadcastMessage(Session session, String body) {
        String[] info = body.split(" ");
        int roomId = Integer.parseInt(info[1]);
        StringBuilder messageBuilder = new StringBuilder(info[3]);
        for (int i = 4;i < info.length;i++) {
            messageBuilder.append(" " + info[i]);
        }
        String broadCastMsg = messageBuilder.toString();

        // Check if broadcast message has illegal words.
        if (Arrays.asList(broadCastMsg.split(" ")).contains("hate")) {
            // Kick out the owner of this room, basically means unload this room.
            unloadRoom(roomId);
            return;
        }

        // Put broadcast message into notification list.
        rooms.get(roomId).getNotifications().add(broadCastMsg);

        // Send back response to all users in this room.
        rooms.get(roomId).getUsers().keySet().stream().forEach(userId -> users.get(userId).getSession().getRemote().sendString(getRoomsForUser(userId).toJson()));
    }

    /**
     * Acknowledge the message from the receiver.
     * @param session the session of the message receiver
     * @param body of format "msgId"
     */
    public void ackMessage(Session session, String body) {
        String[] info = body.split(" ");
        int msgId = Integer.parseInt(info[1]);
        Message message = messages.get(msgId);
        message.setIsReceived(true);

        int senderId = message.getSenderId();

        try {
            users.get(senderId).getSession().getRemote().sendString(getChatBoxForUser(senderId).toJson());
        } catch (IOException excpetion) {
            System.out.println("Failed when sending message received confirmation!");
        }
    }

    /**
     * Send query result from controller to front end.
     * @param session the session that requests to called the method
     * @param body of format "type roomId [senderId] [receiverId]"
     */
    public void query(Session session, String body) {

    }

    /**
     * Notify the client for refreshing.
     * @param user user expected to receive the notification
     * @param response the information for notifying
     */
    public static void notifyClient(User user, AResponse response) {
    }


    /**
     * Notify session about the message.
     * @param session the session to notify
     * @param response the notification information
     */
    public static void notifyClient(Session session, AResponse response) {
    }


    //TODO: The three methods below already exist as a methods in the chatroom class. Do we need them here for some reason?
    /**
     * Get the names of all chat room members.
     * @param roomId the id of the chat room
     * @return all chat room members, mapping from user id to user name
     */
    private Map<Integer, String> getUsers(int roomId) {
        return rooms.get(roomId).getUsers();
    }

    /**
     * Get notifications in the chat room.
     * @param roomId the id of the chat room
     * @return notifications of the chat room
     */
    private List<String> getNotifications(int roomId) {
        return rooms.get(roomId).getNotifications();
    }

    /**
     * Get chat history between user A and user B (commutative).
     * @param roomId the id of the chat room
     * @param userAId the id of user A
     * @param userBId the id of user B
     * @return chat history between user A and user B at a chat room
     */
    private List<Message> getChatHistory(int roomId, int userAId, int userBId) {
        String targetKey = Math.min(userAId, userBId) + "&" + Math.max(userAId, userBId);
        return rooms.get(roomId).getChatHistory().get(targetKey);
    }

    public AResponse getRoomsForUser(int userId) {
        Set<ChatRoom> availableRooms = users.get(userId).getAvailableRoomIds().stream().map(roomId -> rooms.get(roomId)).collect(Collectors.toSet());
        Set<ChatRoom> joinedRooms = users.get(userId).getJoinedRoomIds().stream().filter(roomId -> rooms.get(roomId).getOwner().getId() != userId).map(roomId -> rooms.get(roomId)).collect(Collectors.toSet());
        Set<ChatRoom> ownedRooms = users.get(userId).getJoinedRoomIds().stream().filter(roomId -> rooms.get(roomId).getOwner().getId() != userId).map(roomId -> rooms.get(roomId)).collect(Collectors.toSet());

        return new UserRoomsResponse("UserRooms", userId, ownedRooms, joinedRooms, availableRooms);
    }

    public AResponse getChatBoxForUser(int userId) {
        List<ChatBox> chatBoxes = new LinkedList<>();
        for (int roomId : users.get(userId).getJoinedRoomIds()) {
            ChatRoom room = rooms.get(roomId);

            room.getChatHistory().entrySet().stream().filter(entry -> isRelevant(userId, entry.getKey())).forEach(entry -> chatBoxes.add(new ChatBox(roomId, room.getName(), getAnotherUserId(userId, entry.getKey()), getAnotherUserName(userId, entry.getKey()), entry.getValue() )));
        }
        return new UserChatHistoryResponse("UserChatHistory", chatBoxes);
    }

    private boolean isRelevant(int userId, String key) {
        String[] users = key.split("&");
        if (userId == Integer.parseInt(users[0]) || userId == Integer.parseInt(users[1]))
            return true;

        return false;
    }

    private String getAnotherUserName(int userId, String key) {
        return users.get(getAnotherUserId(userId, key)).getName();
    }

    private int getAnotherUserId(int userId, String key) {
        String[] users = key.split("&");
        if (userId == Integer.parseInt(users[0])) {
            return Integer.parseInt(users[1]);
        } else {
            return Integer.parseInt(users[0]);
        }
    }
}
