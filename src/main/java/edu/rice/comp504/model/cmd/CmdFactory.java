package edu.rice.comp504.model.cmd;

/**
 * Export the command implementation classes to clients outside this package.
 */
public class CmdFactory {
    public static IUserCmd makeAckMessageCmd(String message) {
        return new AckMessageCmd(message.trim());
    }

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

    public static IUserCmd makeLoginCmd(String message) {
        return new LoginCmd(message.trim());
    }

    public static IUserCmd makeQueryChatHistoryOfRoomCmd(String message) {
        return new QueryChatHistoryOfRoomCmd(message.trim());
    }

    public static IUserCmd makeQueryNotificationsOfRoomCmd(String message) {
        return new QueryNotificationsOfRoomCmd(message.trim());
    }

    public static IUserCmd makeQueryUsersOfRoomCmd(String message) {
        return new QueryUsersOfRoomCmd(message.trim());
    }

    public static IUserCmd makeRemoveRoomCmd(String message) {
        return new RemoveRoomCmd(message.trim());
    }

    public static IUserCmd makeSendMessageCmd(String message) {
        return new SendMessageCmd(message.trim());
    }
}
