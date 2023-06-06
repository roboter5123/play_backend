package com.roboter5123.backend.game.implementation.chat;
import com.roboter5123.backend.game.api.Command;
import com.roboter5123.backend.game.api.Game;
import com.roboter5123.backend.game.api.GameMode;
import com.roboter5123.backend.messaging.api.MessageBroadcaster;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class ChatGame implements Game {

    private final ChatState chatState;
    @Getter
    @Setter
    private GameMode gameMode;
    @Getter
    @Setter
    private String sessionCode;
    private final MessageBroadcaster broadcaster;

    public ChatGame(MessageBroadcaster broadcaster) {

        this.broadcaster = broadcaster;
        this.chatState = new ChatState();
        this.gameMode = GameMode.CHAT;
    }

    @Override
    public Map<String, Object> joinGame(final String playerName) {

        this.chatState.join(playerName);
        final Command joinCommand = new Command("server", new ChatJoinAction(playerName));
        this.broadcast(joinCommand);

        return this.chatState.getState();
    }

    @Override
    public void broadcast(Object payload){

        this.broadcaster.broadcastGameUpdate(this.sessionCode, payload);
    }
}