package com.roboter5123.play.backend.seshservice.messaging.implementation;
import com.roboter5123.play.backend.seshservice.messaging.api.StompMessageFactory;
import com.roboter5123.play.backend.seshservice.messaging.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class StompMessageFactoryImpl implements StompMessageFactory {

    public StompMessage getMessage(Object payload) throws UnsupportedOperationException {

        final StompMessage message;

        if (payload instanceof Command command) {

            message = getMessage(command);

        } else if (payload instanceof RuntimeException exception) {

            message = getMessage(exception);

        } else {

            String errorMessage = "Could not create StompMessage. Unsupported payload type";
            log.error(errorMessage);
            throw new UnsupportedOperationException(errorMessage);
        }

        return message;
    }

    private CommandStompMessage getMessage(Command command) {

        final CommandStompMessage message = new CommandStompMessage();
        message.setCommand(command);
        return message;
    }

    public StateStompMessage getMessage(Map<String, Object> seshState) {

        final StateStompMessage message = new StateStompMessage();
        message.setState(seshState);
        return message;
    }

    @Override
    public StompMessage getAckMessage() {

        return new GenericStompMessage();
    }

    private ErrorStompMessage getMessage(RuntimeException exception) {

        final ErrorStompMessage message = new ErrorStompMessage();
        message.setError(exception.getMessage());
        return message;
    }
}
