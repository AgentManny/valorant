package gg.manny.valorant.util.scoreboard;

import com.google.common.base.Preconditions;
import gg.manny.valorant.Valorant;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MScoreboardHandler {

    private static Map<String, PlayerScoreboard> boards = new ConcurrentHashMap<>();
    @Getter @Setter private static ScoreboardAdapter adapter = null;
    private static boolean initiated = false;
    @Getter @Setter private static int updateInterval = 2; // In ticks

    @Getter @Setter private static boolean showHealthBelowPlayer = false;

    // Static class -- cannot be created.
    private MScoreboardHandler() {
    }


    /**
     * Initiates the scoreboard handler.
     * This can only be called once, and is called automatically when Berlin enables.
     */
    public static void init(ScoreboardAdapter adapter) {
        // Only allow the CoreScoreboardHandler to be initiated once.
        // Note the '!' in the .checkState call.
        Preconditions.checkState(!initiated);
        initiated = true;

        setAdapter(adapter);

        (new ScoreboardThread()).start();
        Valorant.getInstance().getServer().getPluginManager().registerEvents(new ScoreboardListener(), Valorant.getInstance());
    }


    /**
     * Creates and registers a new scoreboard. Only for internal use.
     *
     * @param player The player to create the scoreboard for.
     */
    protected static void create(Player player) {
        // We check the configuration here as this is a 'short circuit' to disable
        // the entire system, which is what we want if no one registered a config.
        if (adapter != null) {
            boards.put(player.getName(), new PlayerScoreboard(player));
        }
    }

    /**
     * Updates (ticks) a player's scoreboard. Only for internal use.
     *
     * @param player The player whose scoreboard should be ticked.
     */
    protected static void updateScoreboard(Player player) {
        if (boards.containsKey(player.getName())) {
            boards.get(player.getName()).update();
        }
    }

    /**
     * Removes a player's scoreboard. Only for internal use.
     *
     * @param player The player whose scoreboard should be removed.
     */
    protected static void remove(Player player) {
        boards.remove(player.getName());
    }

}

