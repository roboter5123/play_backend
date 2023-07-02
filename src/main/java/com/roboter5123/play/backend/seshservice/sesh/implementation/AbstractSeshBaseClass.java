package com.roboter5123.play.backend.seshservice.sesh.implementation;
import com.roboter5123.play.backend.seshservice.messaging.api.MessageBroadcaster;
import com.roboter5123.play.backend.seshservice.messaging.model.Command;
import com.roboter5123.play.backend.seshservice.messaging.model.action.Action;
import com.roboter5123.play.backend.seshservice.messaging.model.action.MakeVIPAction;
import com.roboter5123.play.backend.seshservice.messaging.model.action.StartSeshAction;
import com.roboter5123.play.backend.seshservice.sesh.api.PlayerManager;
import com.roboter5123.play.backend.seshservice.sesh.api.Sesh;
import com.roboter5123.play.backend.seshservice.sesh.model.SeshStage;
import com.roboter5123.play.backend.seshservice.sesh.model.SeshType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;

public abstract class AbstractSeshBaseClass implements Sesh {

    @Getter
    private final SeshType seshType;
    @Getter
    @Setter
    private String seshCode;
    private final MessageBroadcaster broadcaster;
    @Getter
    protected LocalDateTime lastInteractionTime;
    protected final Deque<Command> unprocessedCommands;
    protected final PlayerManager playerManager;
    @Getter
    protected SeshStage currentStage;
    protected boolean isStarted;

    protected AbstractSeshBaseClass(MessageBroadcaster broadcaster, SeshType seshType, PlayerManager playerManager) {

        this.broadcaster = broadcaster;
        this.seshType = seshType;
        this.lastInteractionTime = LocalDateTime.now();
        this.unprocessedCommands = new LinkedList<>();
        this.playerManager = playerManager;
        this.currentStage = SeshStage.LOBBY;
        isStarted = false;
    }

    @Override
    public void broadcastToHost(Object payload) {

        this.broadcaster.broadcastSeshUpdateToHost(this.seshCode, payload);
    }

    @Override
    public void broadcastToControllers(Object payload) {

        this.broadcaster.broadcastSeshUpdateToControllers(this.seshCode, payload);
    }

    @Override
    public void broadcastToAll(Object payload) {

        this.broadcaster.broadcastSeshUpdate(this.seshCode, payload);
    }
    protected void processLobbyCommand(Command command) {

        String playerId = command.getPlayerId();
        Action action = command.getAction();

        if (this.playerManager.isVIP(playerId) && action instanceof StartSeshAction) {

            this.currentStage = SeshStage.MAIN;

        } else if ((this.playerManager.isVIP(playerId) || !this.playerManager.hasVIP()) && action instanceof MakeVIPAction makeVIPAction) {

            this.playerManager.setVIP(makeVIPAction.getPlayerId());
        }
    }

    public void startSesh() {

        this.isStarted = true;
    }
}
