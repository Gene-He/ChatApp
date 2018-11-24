package edu.rice.comp504.model.cmd;

import com.google.common.base.Preconditions;
import edu.rice.comp504.model.obj.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class EnforceFilterCmd implements IUserCmd {

    private int roomId;
    private int ageLower;
    private int ageUpper;
    private Set<String> locations;
    private Set<String> schools;

    /**
     * Constructs an instance based on the message from clients.
     *
     * @param message the exact message from webSocket.
     * @Throws throws {@code IllegalArgumentException} if this message is not in required format.
     */
    public EnforceFilterCmd(String message) {
        String[] info = message.split(" ");
        Preconditions.checkArgument(info.length == 6 && info[0].equals("modify"), "Illegal modify room message format: %s", message);

        this.roomId = Integer.parseInt(info[1]);
        this.ageLower = Integer.parseInt(info[2]);
        this.ageUpper = Integer.parseInt(info[3]);
        this.locations = Collections.synchronizedSet(new HashSet<>(Arrays.asList(info[4].split(","))));
        this.schools = Collections.synchronizedSet(new HashSet<>(Arrays.asList(info[4].split(","))));
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
