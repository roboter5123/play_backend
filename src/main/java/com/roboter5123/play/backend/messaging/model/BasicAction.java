package com.roboter5123.play.backend.messaging.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicAction implements Action{

    String playerName;
    String message;
}
