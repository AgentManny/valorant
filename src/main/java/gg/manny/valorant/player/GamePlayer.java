package gg.manny.valorant.player;

import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.game.TeamType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@RequiredArgsConstructor
public class GamePlayer {

    private final UUID uuid;
    private final String name;

    private Agent selectedAgent;
    private TeamType team = TeamType.NONE;

    private PlayerMeta meta = new PlayerMeta();

    private Map<Ability, Integer> cooldowns = new HashMap<>();
    private int ultimatePoints = 0;

    private boolean alive = false;

    private int kills = 0;
    private int assists = 0;

    public void cleanup() { // Activates every new round
        cooldowns.clear();
        meta.reset();
    }

    public void reset() { // Activates when half time they switch.
        cleanup();
        ultimatePoints = 0;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
