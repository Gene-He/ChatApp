package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.obj.ChatRoom;
import edu.rice.comp504.model.obj.User;

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
     * Execute is the function such that all command will execute once the command is passed to observer's update
     *
     * @param context
     * @context a user which the command will operate on
     */
    @Override
    public void execute(User context) {

    }
}
