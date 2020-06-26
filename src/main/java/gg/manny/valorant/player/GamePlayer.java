package gg.manny.valorant.player;

import gg.manny.valorant.agent.Agent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class GamePlayer {

    private final UUID uuid;
    private final String name;

    private Agent selectedAgent;

    private boolean alive = false;

    private int kills = 0;
    private int assists = 0;

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }


}
