package com.roboter5123.backend.messaging.model;
import com.roboter5123.backend.game.Command;
import com.roboter5123.backend.game.GameState;
import com.roboter5123.backend.game.chat.ChatMessageAction;
import com.roboter5123.backend.game.chat.ChatState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class StompMessageFactoryTest {

    @Autowired
    StompMessageFactory messageFactory;
    String playerName;
    String errorMessage;

    @BeforeEach
    void setUp() {

        errorMessage = "This is an error!";
        playerName = "roboter5123";
    }

    @Test
    void GETMESSAGE_WITH_EXCEPTION_SHOULD_RETURN_ERRORSTOMPMESSAGE_WITH_EXCEPTION_MESSAGE() {

        Exception exception = new RuntimeException(errorMessage);
        ErrorStompMessage expected = new ErrorStompMessage();
        expected.setError(exception.getMessage());
        StompMessage result = messageFactory.getMessage(exception);
        assertEquals(expected, result);
    }

    @Test
    void GETMESSAGE_WITH_COMMAND_SHOULD_RETURN_COMMANDSTOMPMESSAGE_WITH_COMMAND() {

        Command command = new Command(playerName, new ChatMessageAction());
        CommandStompMessage expected = new CommandStompMessage();
        expected.setCommand(command);
        StompMessage result = messageFactory.getMessage(command);
        assertEquals(expected, result);
    }

    @Test
    void GETMESSAGE_WITH_GAMESTATE_SHOULD_RETURN_GAMESTATESTOMPMESSAGE_WITH_GAMESTATE() {

        GameState state = new ChatState();
        StateStompMessage expected = new StateStompMessage();
        expected.setState(state);
        StompMessage result = messageFactory.getMessage(state);
        assertEquals(expected, result);
    }

    @Test
    void GETMESSAGE_WITH_NONSUPPORTED_PAYLOAD_SHOULD_THROW_EXCEPTION() {

        Object o = new Object();
        assertThrows(UnsupportedOperationException.class, () -> messageFactory.getMessage(o));
    }
}