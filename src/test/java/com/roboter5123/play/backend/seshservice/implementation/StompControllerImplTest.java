package com.roboter5123.play.backend.seshservice.implementation;
import com.roboter5123.play.backend.seshservice.api.api.StompController;
import com.roboter5123.play.backend.seshservice.messaging.api.MessageBroadcaster;
import com.roboter5123.play.backend.seshservice.messaging.api.StompMessageFactory;
import com.roboter5123.play.backend.seshservice.messaging.model.Command;
import com.roboter5123.play.backend.seshservice.messaging.model.action.BasicAction;
import com.roboter5123.play.backend.seshservice.messaging.model.message.*;
import com.roboter5123.play.backend.seshservice.service.api.SeshManager;
import com.roboter5123.play.backend.seshservice.service.api.SeshService;
import com.roboter5123.play.backend.seshservice.service.exception.NoSuchSeshException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class StompControllerImplTest {

	@MockBean
	SeshService seshServiceMock;
	@MockBean
	MessageBroadcaster broadcasterMock;
	@MockBean
	StompMessageFactory factoryMock;
	@Autowired
	StompController stompController;
	@MockBean
	SeshManager seshManager;

	String sessionCode;
	String playerName;

	String socketId;

	@BeforeEach
	void setUp() {

		sessionCode = "abcd";
		playerName = "roboter5123";
		socketId = "asfd7465asd";
	}

	@Test
	void joinSessionAsHost_should_return_error_message_when_called_with_non_existent_session() {

		NoSuchSeshException exception = new NoSuchSeshException("No session with code " + sessionCode + " exists");
		when(seshServiceMock.joinSeshAsHost(sessionCode, socketId)).thenThrow(exception);

		ErrorStompMessage expected = new ErrorStompMessage(exception.getMessage());
		when(factoryMock.getMessage(exception)).thenReturn(expected);

		StompMessage result = stompController.joinSeshAsHost(sessionCode, socketId);

		assertEquals(expected, result);
	}

	@Test
	void joinSessionAsHost_should_return_state_message_when_called_with_existing_session() {

		Map<String, Object> state = new HashMap<>();
		state.put("a", new ArrayList<>());
		state.put("b", new ArrayList<>());
		when(seshServiceMock.joinSeshAsHost(sessionCode, socketId)).thenReturn(state);

		StompMessage expected = new StateStompMessage(state);
		when(factoryMock.getMessage(any())).thenReturn(expected);

		StompMessage result = stompController.joinSeshAsHost(sessionCode, socketId);

		assertEquals(expected, result);
	}

	@Test
	void joinSessionAsController_should_return_error_message_when_called_with_non_existent_session() {

		NoSuchSeshException exception = new NoSuchSeshException("No session with code " + sessionCode + " exists");
		when(seshServiceMock.joinSeshAsController(sessionCode, playerName, socketId)).thenThrow(exception);

		ErrorStompMessage expected = new ErrorStompMessage(exception.getMessage());
		when(factoryMock.getMessage(exception)).thenReturn(expected);

		StompMessage result = stompController.joinSeshAsController(playerName, sessionCode, socketId);

		assertEquals(expected, result);
	}

	@Test
	void joinSessionAsController_should_return_state_message_when_called_with_existing_session() {

		Map<String, Object> state = new HashMap<>();
		state.put("a", new ArrayList<>());
		state.put("b", new ArrayList<>());
		when(seshServiceMock.joinSeshAsController(sessionCode, playerName, socketId)).thenReturn(state);

		StompMessage expected = new StateStompMessage(state);
		when(factoryMock.getMessage(any())).thenReturn(expected);

		StompMessage result = stompController.joinSeshAsController(playerName, sessionCode, socketId);

		assertEquals(expected, result);
	}

	@Test
	void sendCommandToGame_Should_not_thow_exception_and_call_game_add_command() {

		StompMessage expected = new GenericStompMessage();

		when(factoryMock.getAckMessage()).thenReturn(expected);

		Command incomingCommand = new Command(socketId, new BasicAction(playerName, "Chat message"));
		CommandStompMessage incomingMessage = new CommandStompMessage(incomingCommand);
		StompMessage result = stompController.sendCommandToSesh(incomingMessage, sessionCode);
		assertEquals(expected, result);
		verify(seshServiceMock).sendCommandToSesh(incomingMessage, sessionCode);
	}

	@Test
	void sendCommandToGame_Should_Return_ErrorMessage() {

		NoSuchSeshException exception = new NoSuchSeshException("No Session with code " + sessionCode + " exists");
		doThrow(exception).when(seshServiceMock).sendCommandToSesh(any(), eq(sessionCode));
		when(factoryMock.getMessage(exception)).thenReturn(new ErrorStompMessage(exception.getMessage()));

		StompMessage expected = new ErrorStompMessage(exception.getMessage());

		Command incomingCommand = new Command(playerName, new BasicAction(playerName, "Chat message"));
		CommandStompMessage incomingMessage = new CommandStompMessage(incomingCommand);
		StompMessage result = stompController.sendCommandToSesh(incomingMessage, sessionCode);
		assertEquals(expected, result);
	}
}
