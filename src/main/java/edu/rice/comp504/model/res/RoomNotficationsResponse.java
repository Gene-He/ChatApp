package edu.rice.comp504.model.res;

import java.util.List;

public class RoomNotficationsResponse extends AResponse {

    private int roomId;
    private String roomName;
    private int anotherUserId;
    private String anotherUsername;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public RoomNotficationsResponse(String type, int roomId, String roomName, int anotherUserId, String anotherUsername) {
        super(type);
        this.roomId = roomId;
        this.roomName = roomName;
        this.anotherUserId = anotherUserId;
        this.anotherUsername = anotherUsername;
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

    public String getAnotherUsername() {
        return anotherUsername;
    }

    public void setAnotherUsername(String anotherUsername) {
        this.anotherUsername = anotherUsername;
    }
}
