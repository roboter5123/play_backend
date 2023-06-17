package com.roboter5123.play.backend.game.implementation.chat;
import com.roboter5123.play.backend.game.api.Game;
import com.roboter5123.play.backend.messaging.api.MessageBroadcaster;
import com.roboter5123.play.backend.messaging.model.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class ChatGameTest {

    Game chat;
    String playerName;
    String sessioncode;
    @MockBean
    MessageBroadcaster broadcaster;

    @BeforeEach
    void setUp() {

        broadcaster = Mockito.mock(MessageBroadcaster.class);
        chat = new ChatGame(broadcaster);
        sessioncode = "abcd";
        chat.setSessionCode(sessioncode);
        playerName = "roboter5123";
    }

    @Test
    void joinGame_should_return_gamestate_as_map_string_object() {

        ChatState state = new ChatState();
        state.getChatters().add(playerName);
        state.getChatLog().add(playerName + " joined the Conversation");
        state.setLastCommand(new Command("server", new ChatJoinAction(playerName)));

        Map<String, Object> expected = state.getState();
        Map<String, Object> result = chat.joinGame(playerName);
        assertEquals(expected, result);
    }

    @Test
    void addCommand_should_broadcast_message_and_add_message_to_log(){

        ChatMessageAction incomingAction = new ChatMessageAction("Hello World!");
        Command incomingCommand = new Command(playerName,incomingAction);
        chat.addCommand(incomingCommand);
        verify(broadcaster).broadcastGameUpdate(sessioncode, playerName + ": " + incomingAction.getMessage());
    }
}