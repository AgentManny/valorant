package gg.manny.valorant.player;

import gg.manny.valorant.agent.Agent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GamePlayer {

    private final UUID uuid;
    private final String name;

    @Setter
    private Agent selectedAgent;

    @Setter
    private boolean alive = false;


}
