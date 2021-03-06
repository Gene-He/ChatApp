package edu.rice.comp504.controller;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.DispatcherAdapter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Create a web socket on server side.
 */
@WebSocket
public class WebSocketController {

    public static final String delimiter = "\\|";

    /**
     * Open user's session.
     * @param user The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session user) {
        ChatAppController.getDispatcher().newSession(user);
    }

    /**
     * Send a message.
     * @param user  The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        String[] info = message.split(delimiter);
        Preconditions.checkArgument(info.length > 0, "Illegal client message: %s", message);

        String action = info[0];
        if (action.equals("login")) {
            loginAction(user, message);
        } else if (action.equals("create")) {
            addRoomAction(user, message);
        } else if (action.equals("join")) {
            joinRoomAction(user, message);
        } else if (action.equals("leave")) {
            leaveRoomAction(user, message);
        } else if (action.equals("send")) {
            sendMessageAction(user, message);
        } else if (action.equals("ack")) {
            ackMessageAction(user, message);
        } else if (action.equals("broadcast")) {
            broadcastMessageAction(user, message);
        } else if (info.length > 1) {
            if (action.equals("query") && info[1].equals("roomUsers")) {
                queryUsersOfRoomAction(user, message);
            } else if (action.equals("query") && info[1].equals("roomNotifications")) {
                queryNotificationsOfRoomAction(user, message);
            } else if (action.equals("query") && info[1].equals("userChatHistory")) {
                queryChatHistoryOfRoomAction(user, message);
            }
        }
    }

    /**
     * Close the user's session.
     * @param user The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        DispatcherAdapter dis = ChatAppController.getDispatcher();
        dis.unloadUser(dis.getUserIdFromSession(user));
    }

    /**
     * We define all types of actions for different requests.
     * We can directly call dispatcher's method in onMessage(),
     * but these separate actions make different services clear and give us a chance to check
     * if the message is in required format before actually calling dispatcher.
     * */

    /**
     * user acknowledge receipt of message
     * @Throws throw {@code IllegalArgumentException} if this message is not in required format.
     */
    private void ackMessageAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 2, "Illegal ackMessage message format: %s", message);
        ChatAppController.getDispatcher().ackMessage(user, message.trim());
    }

    /**
     * Handle user creates a new chat room
     * @param user
     * @param message
     */
    private void addRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 6, "Illegal create room message format: %s", message);
        ChatAppController.getDispatcher().loadRoom(user, message.trim());
    }

    /**
     * User joins a room
     * @param user
     * @param message
     */
    private void joinRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 2, "Illegal join room message format: %s", message);
        ChatAppController.getDispatcher().joinRoom(user, message);
    }

    /**
     * User leaves a room
     * @param user
     * @param message
     */
    private void leaveRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 2, "Illegal leave room message format: %s", message);
        ChatAppController.getDispatcher().voluntaryLeaveRoom(user, message);
    }

    /**
     * user query for chat history
     * @param user
     * @param message
     */
    private void queryChatHistoryOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 4, "Illegal QueryChatHistoryOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    /**
     * user query for notification of room
     * @param user
     * @param message
     */
    private void queryNotificationsOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 3, "Illegal QueryNotificationsOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    /**
     * user query for other users in a chat room
     * @param user
     * @param message
     */
    private void queryUsersOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 3, "Illegal QueryUsersOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    /**
     * user sends a message
     * @param user
     * @param message
     */
    private void sendMessageAction(Session user, String message) {
        ChatAppController.getDispatcher().sendMessage(user, message);
    }

    /**
     * For a broadcast request, we have message format like:
     * broadcast roomId message
     * eg:
     * "broadcast 30 Hi, y'all"
     * @param user
     * @param message
     */
    private void broadcastMessageAction(Session user, String message) {
        ChatAppController.getDispatcher().broadcastMessage(user, message);
    }

    /**
     * handle user login
     * @param user
     * @param message
     */
    private void loginAction(Session user, String message) {
        Preconditions.checkArgument(message.split(delimiter).length == 5, "Illegal login message format: %s", message);
        ChatAppController.getDispatcher().newSession(user);
        ChatAppController.getDispatcher().loadUser(user, message);
    }

}
