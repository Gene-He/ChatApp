package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.DispatcherAdapter;
import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;

import java.io.IOException;

/**
 * Command to add a room
 */
class AddRoomCmd implements IUserCmd {

    private ChatRoom chatRoom;
    private DispatcherAdapter da;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public AddRoomCmd(ChatRoom room, DispatcherAdapter da) {
        this.chatRoom = room;
        this.da = da;
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
        if (chatRoom.applyFilter(context)) {
            // Update this user with newly created room if this room is not in this user's available list.
            if (!context.getAvailableRoomIds().stream().anyMatch(roomdId -> roomdId == chatRoom.getId()) && !context.getJoinedRoomIds().stream().anyMatch(roomdId -> roomdId == chatRoom.getId()) ) {
                System.out.println("add available room for user: " + context.getName());
                context.addRoom(chatRoom);

                try {
                    context.getSession().getRemote().sendString(da.getRoomsForUser(context.getId()).toJson());
                } catch (IOException exception) {
                    System.out.println("Failed when sending update that a new room has been created!");
                }

            }
        }
    }
}
