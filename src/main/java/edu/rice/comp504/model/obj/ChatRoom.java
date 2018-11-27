package edu.rice.comp504.model.obj;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.DispatcherAdapter;
import edu.rice.comp504.model.cmd.CmdFactory;

import java.util.*;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
/*
The Chatroom class defines a chat room object and private fileds of a chat room
*/
public class ChatRoom extends Observable {

    private int id;
    private String name;
    private User owner;
    private transient int ageLowerBound;
    private transient int ageUpperBound;
    private String[] locations;
    private String[] schools;

    private transient DispatcherAdapter dis;

    // Maps user id to the user name
    private  Map<Integer, String> userNameFromUserId;

    // notifications contain why the user left, etc.
    private  List<String> notifications;

    // Maps key("smallId&largeId") to list of chat history strings
    private transient Map<String, List<Message>> chatHistory;

    /**
     * Constructor.
     * @param id the identity number of the chat room
     * @param name the name of the chat room
     * @param owner the chat room owner
     * @param lower the lower bound of age restriction
     * @param upper the upper bound of age restriction
     * @param locations the location restriction
     * @param schools the school restriction
     * @param dispatcher the adapter
     */
    public ChatRoom(int id, String name, User owner,
                    int lower, int upper, String[] locations, String[] schools,
                    DispatcherAdapter dispatcher) {
        this.id = id;

        this.name = name;
        this.owner = owner;

        this.ageLowerBound = lower;
        this.ageUpperBound = upper;
        this.locations = locations;
        this.schools = schools;

        this.dis = dispatcher;

        this.userNameFromUserId = new ConcurrentHashMap<>();
        this.notifications = new LinkedList<>();
        this.chatHistory = new ConcurrentHashMap<>();
    }

    /**
     * Get the chat room id
     * @return the chat room id
     * */
    public int getId() {
        return this.id;
    }

    /**
     * Get the chat room name
     * @return the chat room name
     * */
    public String getName() {
        return this.name;
    }

    /**
     * Get the chat room owner
     * @return a User object which is the owner of the chat room
     * */
    public User getOwner() {
        return this.owner;
    }

    /**
     * Get a list of notifications
     * @return notification list
     * */
    public List<String> getNotifications() {
        return this.notifications;
    }

    /**
     * Get the chat history between two users
     * @return chat history
     * */
    public Map<String, List<Message>> getChatHistory() {
        return this.chatHistory;
    }

    /**
     * @return the dispatcher
     * */
    public DispatcherAdapter getDispatcher() {
        return this.dis;
    }

    /**
     * Return users in the chat room
     */
    public Map<Integer, String> getUsers() {
        return userNameFromUserId;
    }

    /**
     * Check if user satisfy the age, location and school restriction
     * @return boolean value indicating whether the user is eligible to join the room
     */
    public boolean applyFilter(User user) {
        if (user == null) return false;
        int age = user.getAge();
        return age >= ageLowerBound && age <= ageUpperBound && checkMeetRestriction(locations,user.getLocation()) &&
                checkMeetRestriction(schools,user.getSchool());
    }

    /**
     * Modify the current room age, location or school restriction
     * Then apply the new restriction to all users in the chat room
     */
    public void modifyFilter(int lower, int upper, String[] locations, String[] schools) {
        ageUpperBound = lower;
        ageLowerBound = upper;
        this.locations = locations;
        this.schools = schools;
    }

    public void addNotification(String s) {notifications.add(s);}

    /**
     * If user satisfy all restrictions and has the room in his available room list
     * Create a user joined notification message and then add user into the observer list
     */
    public boolean addUser(User user) {
        if (userNameFromUserId.containsKey(user.getId()) && !applyFilter(user)){
            return false;
        }
        notifyObservers(CmdFactory.makeJoinRoomCmd(this,user));
        userNameFromUserId.put(user.getId(),user.getName());
        notifications.add(user.getName()+ " joined this room");
        addObserver(user);
        return true;
    }

    /**
     * Remove user from the chat room
     * Set notification indicating the user left reason
     * Delete user from observer list
     */
    public boolean removeUser(User user, String reason) {
        if (!userNameFromUserId.containsKey(user.getId())){
            return false;
        }
        userNameFromUserId.remove(user.getId());
        freeChatHistory(user);
        //TODO: I think we don't need this command below. If all users need to leave the room, this is now handled with the
        //RemoveRoomCmd, issued from the DA.  (-Alex)
        // notifyObservers(CmdFactory.makeLeaveRoomCmd(this,user));
        //TODO: We should ignore the "reason" argument of this method and use addNotification to add messages to the
        //room's list of notifications. I have done this in the DA. (-Alex)
        //notifications.add(user.getName()+ " left this room because " + reason);
        deleteObserver(user);
        return true;
    }

    public void removeAllUsers() {
        userNameFromUserId.clear();
        deleteObservers();
    }

    /**
     * Append chat message into chat history list
     * Map the single message body with key value (senderID&receiverID)
     */
    public void storeMessage(User sender, User receiver, Message message) {
        String key = Math.min(sender.getId(), receiver.getId()) + "&" + Math.max(sender.getId(), receiver.getId());

        if (!chatHistory.containsKey(key)){
            chatHistory.put(key, new ArrayList<>());
        }
        chatHistory.get(key).add(message);
    }

    /**
     * Parse the key and remove chat history related to user
     */
    private void freeChatHistory(User user) {
        for(Iterator<Map.Entry<String,List<Message>>> it = chatHistory.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String,List<Message>> entry = it.next();
            String[] users = entry.getKey().split("&");
            Preconditions.checkArgument(users.length == 2,"Illegal key of chatHistory");
            if (users[0].equals(user.getId()) || users[1].equals(user.getId())){
                it.remove();
            }
        }
    }

    private boolean checkMeetRestriction(String[] arr, String candidate) {
        if (arr == null) {
            return true;
        }
        if (candidate == null) {
            return false;
        }
        for (String s : arr) {
            if (candidate.equals(s)) {
                return true;
            }
        }
        return false;
    }
}

