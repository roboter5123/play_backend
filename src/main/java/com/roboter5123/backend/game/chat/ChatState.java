package com.roboter5123.backend.game.chat;
import com.roboter5123.backend.game.Command;
import com.roboter5123.backend.game.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatState implements GameState {

    private List<String> chatter = new ArrayList<>();
    private List<String> chatLog = new ArrayList<>();
    private Command lastCommand;

    public void join(String playerName) {

        this.chatter.add(playerName);
        this.chatLog.add(playerName + " joined the Conversation");
        this.lastCommand = new ChatMessageCommand("server", new ChatMessageAction(ChatActionType.JOIN, playerName));
    }

    @Override
    public Map<String, Object> getState() {

        Map<String, Object> state = new HashMap<>();
        state.put("chatlog", this.chatLog);
        state.put("chatters", this.chatter);
        return state;
    }
}
