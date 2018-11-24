package edu.rice.comp504.model.res;

public class NewUserResponse extends AResponse {

    private int userId;
    private int username;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public NewUserResponse(String type, int userId, int username) {
        super(type);
        this.userId = userId;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}
