package edu.rice.comp504.model.res;

import edu.rice.comp504.model.obj.ChatBox;

import java.util.List;

public class UserChatHistoryResponse extends AResponse {

    private String username;
    private List<ChatBox> chatHistory;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public UserChatHistoryResponse(String type, String username, List<ChatBox> chatHistory) {
        super(type);
        this.username = username;
        this.chatHistory = chatHistory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ChatBox> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<ChatBox> chatHistory) {
        this.chatHistory = chatHistory;
    }
}
