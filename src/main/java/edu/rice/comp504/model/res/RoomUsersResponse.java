package edu.rice.comp504.model.res;

import java.util.Set;

public class RoomUsersResponse extends AResponse {

    private int roomId;
    private Set<Integer> userIds;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public RoomUsersResponse(String type, int roomId, Set<Integer> userIds) {
        super(type);
        this.roomId = roomId;
        this.userIds = userIds;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }
}
