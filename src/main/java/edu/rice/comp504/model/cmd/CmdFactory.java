package edu.rice.comp504.model.cmd;

/**
 * Export the command implementation classes to clients outside this package.
 */
public class CmdFactory {

    public static IUserCmd makeAddRoomCmd(String message) {
        return new AddRoomCmd(message.trim());
    }

    public static IUserCmd makeEnforceFilterCmd(String message) {
        return new EnforceFilterCmd(message.trim());
    }

    public static IUserCmd makeJoinRoomCmd(String message) {
        return new JoinRoomCmd(message.trim());
    }

    public static IUserCmd makeLeaveRoomCmd(String message) {
        return new LeaveRoomCmd(message.trim());
    }

    public static IUserCmd makeRemoveRoomCmd(String message) {
        return new RemoveRoomCmd(message.trim());
    }
}
