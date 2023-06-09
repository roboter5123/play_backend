package com.roboter5123.play.backend.seshservice.messaging.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private String player;
    private Action action;
}
