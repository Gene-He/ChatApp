package edu.rice.comp504.model.cmd;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.obj.User;

class AckMessageCmd implements IUserCmd {

    private int messageId;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param message the exact message from webSocket.
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public AckMessageCmd(String message) {
        String[] info = message.split(" ");
        Preconditions.checkArgument(info.length == 2 && info[0].equals("ack"), "Illegal ackMessage message format: %s", message);

        this.messageId = Integer.parseInt(info[1]);
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
