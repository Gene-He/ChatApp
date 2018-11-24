package edu.rice.comp504.model.cmd;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.obj.User;

class QueryUsersOfRoomCmd implements IUserCmd {

    private int roomId;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param message the exact message from webSocket.
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public QueryUsersOfRoomCmd(String message) {
        String[] info = message.split(" ");
        Preconditions.checkArgument(info.length == 3 && info[0].equals("query") && info[1].equals("roomUsers"), "Illegal QueryUsersOfRoom message format: %s", message);

        this.roomId = Integer.parseInt(info[2]);
    }
    /**
     * Execute is the function such that all command will execute once the command is passed to observer's update
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {

    }
}
