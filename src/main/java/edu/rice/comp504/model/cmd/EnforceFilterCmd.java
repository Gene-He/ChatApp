package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;

import java.util.stream.Stream;

/**
 * Command to filter user in a chat room
 */
class EnforceFilterCmd implements IUserCmd {

    private ChatRoom chatRoom;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param room the new room constructed by dispatcher
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public EnforceFilterCmd(ChatRoom room) {
        this.chatRoom = room;
    }

    /**
     * This chatRoom has changed its restrictions, check if the user passed in need to update itself.
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {
        boolean isInJoinedOrAvailableRoom = Stream.concat(context.getJoinedRoomIds().stream(), context.getAvailableRoomIds().stream()).anyMatch(roomId -> roomId == chatRoom.getId());

        if (chatRoom.applyFilter(context)) {
            // This user is qualified to join this room.
            if (!isInJoinedOrAvailableRoom) {
                context.addRoom(chatRoom);
            }
        } else {
            if (isInJoinedOrAvailableRoom) {
                context.removeRoom(chatRoom);
            }
        }
    }
}
