package edu.rice.comp504.model.res;

import edu.rice.comp504.model.obj.ChatBox;

import java.util.List;

public class UserChatHistoryResponse extends AResponse {

    private List<ChatBox> chatHistory;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public UserChatHistoryResponse(String type, List<ChatBox> chatHistory) {
        super(type);
        this.chatHistory = chatHistory;
    }

    public List<ChatBox> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<ChatBox> chatHistory) {
        this.chatHistory = chatHistory;
    }
}
