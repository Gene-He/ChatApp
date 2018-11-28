package edu.rice.comp504.model.obj;

import java.util.List;

/**
 * Represent a chat between two users
 */
public class ChatBox {

    private int roomId;
    private String roomName;
    private int anotherUserId;
    private String anotherUserName;

    private List<Message> chatHistory;

    public ChatBox(int roomId, String roomName, int userId, String username, List<Message> chatHistory) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.anotherUserId = userId;
        this.anotherUserName = username;
        this.chatHistory = chatHistory;
    }
}
