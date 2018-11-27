package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.DispatcherAdapter;
import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;

/**
 * Export the command implementation classes to clients outside this package.
 */
public class CmdFactory {

    public static IUserCmd makeAddRoomCmd(ChatRoom chatRoom, DispatcherAdapter da) {
        return new AddRoomCmd(chatRoom, da);
    }

    public static IUserCmd makeEnforceFilterCmd(ChatRoom chatRoom) {
        return new EnforceFilterCmd(chatRoom);
    }

    public static IUserCmd makeJoinRoomCmd(ChatRoom chatRoom, User user) {
        return new JoinRoomCmd(chatRoom, user);
    }

    public static IUserCmd makeLeaveRoomCmd(ChatRoom chatRoom, User user) {
        return new LeaveRoomCmd(chatRoom, user);
    }

    public static IUserCmd makeRemoveRoomCmd(ChatRoom chatRoom, DispatcherAdapter da) {
        return new RemoveRoomCmd(chatRoom, da);
    }
}
