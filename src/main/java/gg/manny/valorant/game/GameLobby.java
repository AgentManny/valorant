package gg.manny.valorant.game;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.map.GameMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.stream.Collectors;

// - - - - Lobby logic - - - - -
// Players teleport to waiting room (Use default world ("world") spawn location)
// Only able to select Team A or Team B (not agents until game is ready)
//
// Allow voting for a map (book that shows maps inside)
// Choose highest vote or random map is picked if no one votes (rare but could happen)
//
// No team selected (pick fair [even teams] random -- don't put into spectator)
//
// Lobby timer done (teleport them into the map)
// They are now able to select their agents (won't be able to pick any selected ones)
// They also won't be able to move. (slowness or maybe on invisible vehicle)
// Don't pick in time == Random agent (60s)
// We can also show on scoreboard on how many people are ready
//
// Game starts.
public class GameLobby implements Listener {

    public static final int STARTING_TIMER = 20;
    public static final int GAME_TIMER = 100;

    public static final int REQUIRED_PLAYERS = 2; // DEFAULT IS 10

    private final Valorant plugin;
    private final Game game;

    public Map<String, List<UUID>> votes = new LinkedHashMap<>(); // Sorted to ensure Random is always first

    public GameLobby(Valorant plugin) {
        this.plugin = plugin;
        this.game = plugin.getGame();

        votes.put("Random", new ArrayList<>());
        for (GameMap map : plugin.getMapManager().getMaps()) {
            votes.put(map.getName(), new ArrayList<>());
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /** Returns what map a player has selected */
    public boolean hasVoted(Player player, String map) {
        for (Map.Entry<String, List<UUID>> entry : votes.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(map) && entry.getValue().contains(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void setVote(Player player, String map) {
        for (Map.Entry<String, List<UUID>> entry : votes.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(map)) {
                entry.getValue().remove(player.getUniqueId());
            }
        }

        if (map != null) {
            votes.get(map).add(player.getUniqueId());
        }
    }


    public Map<String, Integer> getVotes() {
        return votes.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size())).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    public void checkPlayerRequirements() {
        if (game.getState() == GameState.STARTING || game.getState() == GameState.WAITING) {
            boolean requiredPlayers = REQUIRED_PLAYERS <= Bukkit.getOnlinePlayers().size();
            GameState state = game.getState();
            if (requiredPlayers) {
                if (state == GameState.WAITING) {
                    game.setState(GameState.STARTING);
                    game.setTimer(STARTING_TIMER);
                    Bukkit.broadcastMessage(Locale.BROADCAST_PREFIX + "Game starting in " + ChatColor.LIGHT_PURPLE + game.getTimer() + " seconds" + ChatColor.WHITE + ".");
                }
            } else if (state == GameState.STARTING) {
                game.setTimer(-1);
                game.setState(GameState.WAITING);
                Bukkit.broadcastMessage(Locale.BROADCAST_PREFIX + "Game delayed, there isn't enough players.");
            }
        }
    }

}
