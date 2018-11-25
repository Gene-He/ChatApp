package edu.rice.comp504.controller;

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

    private void EnforceFilterAction(Session user, String message) {

    }

    private void JoinRoomAction(Session user, String message) {

    }

    private void LeaveRoomAction(Session user, String message) {

    }

    private void QueryChatHistoryOfRoomAction(Session user, String message) {

    }

    private void QueryNotificationsOfRoomAction(Session user, String message) {

    }

    private void QueryUsersOfRoomAction(Session user, String message) {

    }

    private void removeRoomAction(Session user, String message) {

    }

    private void sendMessageAction(Session user, String message) {

    }

    private void loginAction(Session user, String message) {

    }

    private void

}
