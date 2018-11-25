package edu.rice.comp504.controller;

import com.google.common.base.Preconditions;
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
        } else if (info[0].equals("remove")) {
            removeRoomAction(user, message);
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

    private void ackMessageAction(Session user, String message) {

    }

    private void addRoomAction(Session user, String message) {

    }

    private void enforceFilterAction(Session user, String message) {

    }

    private void joinRoomAction(Session user, String message) {

    }

    private void leaveRoomAction(Session user, String message) {

    }

    private void queryChatHistoryOfRoomAction(Session user, String message) {

    }

    private void queryNotificationsOfRoomAction(Session user, String message) {

    }

    private void queryUsersOfRoomAction(Session user, String message) {

    }

    private void removeRoomAction(Session user, String message) {

    }

    private void sendMessageAction(Session user, String message) {

    }

    private void loginAction(Session user, String message) {

    }

}
