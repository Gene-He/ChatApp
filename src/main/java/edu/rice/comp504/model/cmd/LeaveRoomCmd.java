package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;
import edu.rice.comp504.model.res.AResponse;
import edu.rice.comp504.model.res.RoomNotficationsResponse;
import edu.rice.comp504.model.res.RoomUsersResponse;

import java.io.IOException;
import java.util.stream.Stream;

class LeaveRoomCmd implements IUserCmd {

    private ChatRoom chatRoom;
    private User user;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public LeaveRoomCmd(ChatRoom room, User user) {
        this.chatRoom = room;
        this.user = user;
    }
    /**
     * There are two cases here:
     * 1. owner of this chatRoom leaves this room (destroy this room in this case).
     * 2. one user who is not the owner leaves this room.
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {
        boolean isInJoinedOrAvailableRoom = false;
        isInJoinedOrAvailableRoom = Stream.concat(context.getJoinedRoomIds().stream(), context.getAvailableRoomIds().stream()).anyMatch(roomId -> roomId == chatRoom.getId());
        boolean isInJoinedRoom = false;
        isInJoinedRoom = context.getJoinedRoomIds().stream().anyMatch(roomId -> roomId == chatRoom.getId());

        if (isInJoinedOrAvailableRoom) {
            // This context user has this room.
            if (user.getId() == chatRoom.getOwner().getId() || chatRoom.getUsers().size() == 0) {
                // Leaving user is the owner of this room or there are no users left in this room.
                user.removeRoom(chatRoom);
            } else if (isInJoinedRoom) {
                // Constructs a RoomUsersResponse.
                AResponse res = new RoomUsersResponse("RoomUsers", chatRoom.getId(), chatRoom.getUsers().keySet());

                try {
                    context.getSession().getRemote().sendString(res.toJson());
                } catch (IOException exception) {
                    System.out.println("Failed when trying to update user list of roomId: " + chatRoom.getId() + " for userId: " + context.getId());
                }

                // Constructs a RoomNotificationsResponse to notify users in the same room why that user left.
                AResponse notficationsResponse = new RoomNotficationsResponse("RoomNotifications", chatRoom.getId(), chatRoom.getNotifications());

                try {
                    context.getSession().getRemote().sendString(notficationsResponse.toJson());
                } catch (IOException exception) {
                    System.out.println("Failed when trying to notify leaving reason: "+ chatRoom.getId() + " for userId: " + context.getId());
                }
            }
        }
    }
}
