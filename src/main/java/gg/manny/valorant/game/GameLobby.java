package gg.manny.valorant.game;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.menu.AgentSelector;
import gg.manny.valorant.team.menu.TeamSelectMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.Slot;
import org.ipvp.ingot.type.VanillaHotbar;

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

    public static Hotbar LOBBY_HOTBAR = new VanillaHotbar();

    private final Valorant plugin;
    private final Game game;

    public GameLobby(Valorant plugin) {
        this.plugin = plugin;
        this.game = plugin.getGame();

        loadHotbars();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void loadHotbars() {
        Slot slot = LOBBY_HOTBAR.getSlot(1);
        slot.setItem(new ItemStack(Material.BOOK));
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new AgentSelector().openMenu(player);
            }
        });

        slot = LOBBY_HOTBAR.getSlot(0);
        slot.setItem(new ItemStack(Material.CLOCK));
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new TeamSelectMenu().openMenu(player);
            }
        });
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
