package edu.rice.comp504.model.cmd;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.obj.User;

/**
 * Command for user login.
 */
class LoginCmd implements IUserCmd {
    private String username;
    private int age;
    private String location;
    private String school;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param message the exact message from webSocket.
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public LoginCmd(String message) {
        String[] info = message.split(" ");
        Preconditions.checkArgument(info.length == 5 && info[0].equals("login"), "Illegal login message format: %s", message);

        this.username = info[1];
        this.age = Integer.parseInt(info[2]);
        this.location = info[3];
        this.school = info[4];
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
