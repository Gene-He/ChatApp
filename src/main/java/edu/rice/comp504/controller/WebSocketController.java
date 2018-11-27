package edu.rice.comp504.controller;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.DispatcherAdapter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketController {

    /**
     * Open user's session.
     * @param user The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session user) {

    }


    /**
     * Send a message.
     * @param user  The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        String[] info = message.split(" ");
        Preconditions.checkArgument(info.length > 0, "Illegal client message: %s", message);

        if (info[0].equals("login")) {
            loginAction(user, message);
        } else if (info[0].equals("create")) {
            addRoomAction(user, message);
        } else if (info[0].equals("modify")) {
            enforceFilterAction(user, message);
        } else if (info[0].equals("join")) {
            joinRoomAction(user, message);
        } else if (info[0].equals("leave")) {
            leaveRoomAction(user, message);
        } else if (info[0].equals("send")) {
            sendMessageAction(user, message);
        } else if (info[0].equals("ack")) {
            ackMessageAction(user, message);
        } else if (info[0].equals("query") && info[1].equals("roomUsers")) {
            queryUsersOfRoomAction(user, message);
        } else if (info[0].equals("query") && info[1].equals("roomNotifications")) {
            queryNotificationsOfRoomAction(user, message);
        } else if (info[0].equals("query") && info[1].equals("userChatHistory")) {
            queryChatHistoryOfRoomAction(user, message);
        }
    }

    /**
     * Close the user's session.
     * @param user The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {

    }

    /**
     * We define all types of actions for different requests.
     * We can directly call dispatcher's method in onMessage(), but these separate actions make different services clear and give us a chance to check
     * if the message is in required format before actually calling dispatcher.
     *
     * @Throws throw {@code IllegalArgumentException} if this message is not in required format.
     */
    private void ackMessageAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 2, "Illegal ackMessage message format: %s", message);
        ChatAppController.getDispatcher().ackMessage(user, message.trim());
    }

    private void addRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 6, "Illegal create room message format: %s", message);
        ChatAppController.getDispatcher().loadRoom(user, message.trim());
    }

    private void enforceFilterAction(Session user, String message) {
//        Preconditions.checkArgument(message.split(" ").length == 6, "Illegal modify room message format: %s", message);
//        ChatAppController.getDispatcher().modifyRoom(user, message.trim());
    }

    private void joinRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 2, "Illegal join room message format: %s", message);
        ChatAppController.getDispatcher().joinRoom(user, message);
    }

    private void leaveRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 2, "Illegal leave room message format: %s", message);
        ChatAppController.getDispatcher().leaveRoom(user, message);
    }

    private void queryChatHistoryOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 4, "Illegal QueryChatHistoryOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    private void queryNotificationsOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 3, "Illegal QueryNotificationsOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    private void queryUsersOfRoomAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 3, "Illegal QueryUsersOfRoom message format: %s", message);
        ChatAppController.getDispatcher().query(user, message);
    }

    private void sendMessageAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 4, "Illegal sendMessage message format: %s", message);
        ChatAppController.getDispatcher().sendMessage(user, message);
    }

    private void loginAction(Session user, String message) {
        Preconditions.checkArgument(message.split(" ").length == 5, "Illegal login message format: %s", message);
        ChatAppController.getDispatcher().loadUser(user, message);
    }

}
