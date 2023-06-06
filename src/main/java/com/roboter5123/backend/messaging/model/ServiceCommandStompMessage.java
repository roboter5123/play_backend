package com.roboter5123.backend.messaging.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCommandStompMessage implements StompMessage {

    ServiceCommand command;
}