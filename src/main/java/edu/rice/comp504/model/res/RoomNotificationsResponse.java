package edu.rice.comp504.model.res;

/**
 * Represent a notification in a chat room
 */
public class RoomNotificationsResponse extends AResponse {

    private int roomId;
    private String roomName;
    private int anotherUserId;
    private String anotherUsername;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public RoomNotificationsResponse(String type, int roomId, String roomName, int anotherUserId, String anotherUsername) {
        super(type);
        this.roomId = roomId;
        this.roomName = roomName;
        this.anotherUserId = anotherUserId;
        this.anotherUsername = anotherUsername;
    }
}
