package edu.rice.comp504.model.res;

import java.util.Set;

public class UserRoomsResponse extends AResponse {

    private int userId;
    private Set<Integer> joinedRoomIds;
    private Set<Integer> availableRoomIds;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public UserRoomsResponse(String type, int userId, Set<Integer> joinedRoomIds, Set<Integer> availableRoomIds) {
        super(type);
        this.userId = userId;
        this.joinedRoomIds = joinedRoomIds;
        this.availableRoomIds = availableRoomIds;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<Integer> getJoinedRoomIds() {
        return joinedRoomIds;
    }

    public void setJoinedRoomIds(Set<Integer> joinedRoomIds) {
        this.joinedRoomIds = joinedRoomIds;
    }

    public Set<Integer> getAvailableRoomIds() {
        return availableRoomIds;
    }

    public void setAvailableRoomIds(Set<Integer> availableRoomIds) {
        this.availableRoomIds = availableRoomIds;
    }
}
