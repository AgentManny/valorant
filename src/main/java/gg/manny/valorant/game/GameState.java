package gg.manny.valorant.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameState {

    WAITING("Pre-Lobby", "Waiting...", false),
    STARTING("Lobby", "Starting in", true),
    STARTED("In Game", "Game Time", false),
    FINISHED("In Game", "Game Time", false);

    private String friendlyName;
    private String name;
    private boolean countdown;
}
