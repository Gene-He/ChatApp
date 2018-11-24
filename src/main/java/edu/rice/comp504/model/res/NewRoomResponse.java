package edu.rice.comp504.model.res;

public class NewRoomResponse extends AResponse {

    private int roomId;
    private String roomName;
    private int ownerId;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public NewRoomResponse(String type, int roomId, String roomName, int ownerId) {
        super(type);
        this.roomId = roomId;
        this.roomName = roomName;
        this.ownerId = ownerId;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
