package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;
import edu.rice.comp504.model.res.AResponse;
import edu.rice.comp504.model.res.RoomUsersResponse;

import java.io.IOException;

class JoinRoomCmd implements IUserCmd {

    private ChatRoom chatRoom;
    private User user;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public JoinRoomCmd(ChatRoom room, User user) {
        this.chatRoom = room;
        this.user = user;
    }
    /**
     * Users whose joined rooms or available rooms that contains this chatRoom need to update the user list
     * It depends on if this user open this chatRoom in front end, but we just send this updated user list to front end
     * since we don't know if this user open this chatRoom in front end right now. Whether to take action when receiving this response depends on front end.
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {
        if (user.getId() == context.getId()) {
            return;
        }
        boolean isInJoinedRoom = false;
        isInJoinedRoom = context.getJoinedRoomIds().stream().anyMatch(roomId -> roomId == chatRoom.getId());

        //Constructs a RoomUsersResponse.
        AResponse res = new RoomUsersResponse("RoomUsers", chatRoom.getId(), chatRoom.getUsers().keySet());

        try {
            context.getSession().getRemote().sendString(res.toJson());
        } catch (IOException exception) {
            System.out.println("Failed when trying to update user list of roomId: " + chatRoom.getId() + " for userId: " + context.getId());
        }
    }
}
