package com.roboter5123.backend.messaging.model;
import com.roboter5123.backend.game.Command;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommandStompMessage extends StompMessage{

    Command body;
}
