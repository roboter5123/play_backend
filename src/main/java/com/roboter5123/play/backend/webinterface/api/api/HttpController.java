package com.roboter5123.play.backend.webinterface.api.api;
import com.roboter5123.play.backend.game.api.GameMode;
import com.roboter5123.play.backend.webinterface.api.model.HttpGameDTO;
import com.roboter5123.play.backend.webinterface.api.model.exception.NoSuchSessionHttpException;
import com.roboter5123.play.backend.webinterface.api.model.exception.TooManySessionsHttpException;

import java.util.List;

public interface HttpController {

    List<GameMode> getGameModes();

    HttpGameDTO createSession(GameMode gameMode) throws TooManySessionsHttpException;

    HttpGameDTO getGame(String sessionCode) throws NoSuchSessionHttpException;
}
