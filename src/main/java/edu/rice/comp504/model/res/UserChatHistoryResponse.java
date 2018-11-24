package edu.rice.comp504.model.res;

import edu.rice.comp504.model.obj.Message;

import java.util.List;

public class UserChatHistoryResponse extends AResponse {

    private List<Message> chatHistory;

    /**
     * Constructor.
     *
     * @param type the type of the response, i.e. the name of class
     */
    public UserChatHistoryResponse(String type, List<Message> chatHistory) {
        super(type);
        this.chatHistory = chatHistory;
    }

    public List<Message> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<Message> chatHistory) {
        this.chatHistory = chatHistory;
    }
}
