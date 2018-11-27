package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.DispatcherAdapter;
import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;

import java.io.IOException;
import java.util.stream.Stream;

class RemoveRoomCmd implements IUserCmd {

    private ChatRoom chatRoom;
    private DispatcherAdapter da;
    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public RemoveRoomCmd(ChatRoom room, DispatcherAdapter da) {
        this.chatRoom = room;
        this.da = da;
    }

    /**
     * Execute is the function such that all command will execute once the command is passed to observer's update
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {
        boolean isInJoinedOrAvailableRoom = false;
        isInJoinedOrAvailableRoom = Stream.concat(context.getJoinedRoomIds().stream(), context.getAvailableRoomIds().stream()).anyMatch(roomId -> roomId == chatRoom.getId());

        if (isInJoinedOrAvailableRoom) {
            context.removeRoom(chatRoom);
            try {
                context.getSession().getRemote().sendString(da.getRoomsForUser(context.getId()).toJson());
                context.getSession().getRemote().sendString(da.getChatBoxForUser(context.getId()).toJson());
            } catch (IOException exception) {
                System.out.println("Failed when sending update that a room has been removed!");
            }

        }
    }
}
