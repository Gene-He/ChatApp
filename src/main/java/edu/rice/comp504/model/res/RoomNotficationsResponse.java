package edu.rice.comp504.model.res;

import java.util.List;

public class RoomNotficationsResponse extends AResponse {

    private int roomId;
    private List<String> notifications;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public RoomNotficationsResponse(String type, int roomId, List<String> notifications) {
        super(type);
        this.roomId = roomId;
        this.notifications = notifications;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
}
