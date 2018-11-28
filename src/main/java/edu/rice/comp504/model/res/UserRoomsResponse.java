package edu.rice.comp504.model.res;

import edu.rice.comp504.model.obj.ChatRoom;
import java.util.Set;

/**
 * Represent different type of rooms for a user
 */
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
}
