package com.roboter5123.play.backend.seshservice.sesh.implementation.chat;
import com.roboter5123.play.backend.seshservice.messaging.api.MessageBroadcaster;
import com.roboter5123.play.backend.seshservice.messaging.model.Action;
import com.roboter5123.play.backend.seshservice.messaging.model.Command;
import com.roboter5123.play.backend.seshservice.sesh.api.SeshType;
import com.roboter5123.play.backend.seshservice.sesh.exception.PlayerAlreadyJoinedException;
import com.roboter5123.play.backend.seshservice.sesh.implementation.AbstractSeshBaseClass;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.Map;

@Log4j2
@ToString
public class ChatSesh extends AbstractSeshBaseClass {

    private final ChatState chatState;

    public ChatSesh(String seshCode, MessageBroadcaster broadcaster) {

        super(seshCode,broadcaster, SeshType.CHAT);
        this.chatState = new ChatState();

    }

    @Override
    public Map<String, Object> joinSesh(final String playerName) throws PlayerAlreadyJoinedException {

        String message = this.chatState.join(playerName);
        ChatJoinAction action = new ChatJoinAction(playerName, message);
        final Command joinMessageCommand = new Command("server", action);
        this.broadcast(joinMessageCommand);
        this.lastInteractionTime = LocalDateTime.now();

        return this.chatState.getState();
    }

    @Override
    public void addCommand(final Command command) throws UnsupportedOperationException {

        Action action = command.getAction();
        this.lastInteractionTime = LocalDateTime.now();

        if (action instanceof ChatMessageAction chatMessageAction) {

            addMessage(command.getPlayer(), chatMessageAction);

        } else {

            String errorMessage = "Could not execute action. Unsupported action type";
            log.error(errorMessage);
            throw new UnsupportedOperationException(errorMessage);
        }
    }

    private void addMessage(String playerName, ChatMessageAction action) {

        final String message = this.chatState.addMessage(playerName, action.getMessage());
        this.broadcast(message);
    }
}
