package edu.rice.comp504.model.obj;

import java.util.List;

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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getAnotherUserId() {
        return anotherUserId;
    }

    public void setAnotherUserId(int anotherUserId) {
        this.anotherUserId = anotherUserId;
    }

    public String getAnotherUserName() {
        return anotherUserName;
    }

    public void setAnotherUserName(String anotherUserName) {
        this.anotherUserName = anotherUserName;
    }

    public List<Message> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<Message> chatHistory) {
        this.chatHistory = chatHistory;
    }
}
