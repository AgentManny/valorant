package gg.manny.valorant.player;

import gg.manny.valorant.Valorant;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Valorant plugin;

    @Getter
    private Map<UUID, GamePlayer> playerMap = new HashMap<>();

    public PlayerManager(Valorant plugin) {
        this.plugin = plugin;
    }

    public GamePlayer getOrCreate(UUID id, String name, boolean cache) {
        GamePlayer gamePlayer = getById(id);
        return gamePlayer == null ? create(id, name, cache) : gamePlayer;
    }

    public GamePlayer create(UUID id, String name, boolean cache) {
        GamePlayer gamePlayer = new GamePlayer(id, name);
        if (!cache) { // todo add saving stuff such as player data
            playerMap.put(id, gamePlayer);
        }
        return gamePlayer;
    }

    // Getting player stuff
    public GamePlayer getByPlayer(Player player) {
        return player == null ? null : getById(player.getUniqueId());
    }

    public GamePlayer getByName(String name) {
        Player player = plugin.getServer().getPlayer(name);
        return getByPlayer(player);
    }

    public GamePlayer getById(UUID id) {
        return playerMap.get(id);
    }
}
