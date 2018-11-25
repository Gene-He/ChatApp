package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;
import edu.rice.comp504.model.res.AResponse;
import edu.rice.comp504.model.res.UserRoomsResponse;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.HashSet;

class AddRoomCmd implements IUserCmd {

    private ChatRoom chatRoom;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public AddRoomCmd(ChatRoom room) {
       this.chatRoom = room;
    }

    /**
     * Execute is the function such that all command will execute once the command is passed to observer's update
     *
     * @param context
     * @context a user which the command will operate on
     *
     * @Throws throws {@code IOException} if failed when send message back to this user
     */
    @Override
    public void execute(User context) {
        if (chatRoom.getOwner().getId() == context.getId())
            return;
        // Only if this user is qualified for this newly created room, we add this new room to this user's available room list.
        if (chatRoom.applyFilter(context)) {
            // Update this user with newly created room.
            context.addRoom(chatRoom);

            // Constructs a UserRoomsResponse and send it to this user.
            AResponse res = new UserRoomsResponse("UserRooms", context.getId(), new HashSet<>(context.getJoinedRoomIds()), new HashSet<>(context.getAvailableRoomIds()));

            try {
                context.getSession().getRemote().sendString(res.toJson());
            } catch (IOException exception) {
                // Just print out what happened without trying again.
                System.out.println("Failed when sending updated rooms to userId: " + context.getId());
            }
        }
    }
}
