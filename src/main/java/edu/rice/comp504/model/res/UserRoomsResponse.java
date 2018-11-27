package edu.rice.comp504.model.res;

import edu.rice.comp504.model.obj.ChatRoom;

import java.util.Set;

public class UserRoomsResponse extends AResponse {

    private int userId;
    private String username;
    private Set<ChatRoom> ownedRooms;
    private Set<ChatRoom> joinedRooms;
    private Set<ChatRoom> availableRooms;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public UserRoomsResponse(String type, int userId, String username, Set<ChatRoom> ownedRooms, Set<ChatRoom> joinedRooms, Set<ChatRoom> availableRooms) {
        super(type);
        this.userId = userId;
        this.username = username;
        this.ownedRooms = ownedRooms;
        this.joinedRooms = joinedRooms;
        this.availableRooms = availableRooms;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<ChatRoom> getOwnedRooms() {
        return ownedRooms;
    }

    public void setOwnedRooms(Set<ChatRoom> ownedRooms) {
        this.ownedRooms = ownedRooms;
    }

    public Set<ChatRoom> getJoinedRooms() {
        return joinedRooms;
    }

    public void setJoinedRooms(Set<ChatRoom> joinedRooms) {
        this.joinedRooms = joinedRooms;
    }

    public Set<ChatRoom> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Set<ChatRoom> availableRooms) {
        this.availableRooms = availableRooms;
    }
}
