package gg.manny.valorant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.manny.valorant.agent.AgentManager;
import gg.manny.valorant.command.MapCommand;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.listener.PlayerListener;
import gg.manny.valorant.map.MapManager;
import gg.manny.valorant.player.PlayerManager;
import gg.manny.valorant.sidebar.GameSidebar;
import gg.manny.valorant.team.TeamManager;
import gg.manny.valorant.util.scoreboard.MScoreboardHandler;
import lombok.Getter;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.ingot.HotbarFunctionListener;
import org.spigotmc.SpigotConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Getter
public class Valorant extends JavaPlugin {

    public static final Random RANDOM = new Random();
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private static Valorant instance;

    private Game game;
    private GameLobby lobby;

    private MapManager mapManager;
    private AgentManager agentManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;

        game = new Game(this);
        lobby = new GameLobby(this);

        mapManager = new MapManager(this);

        agentManager = new AgentManager(this);

        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);

        registerCommands();

        Arrays.asList(
                new PlayerListener(this),
                new HotbarFunctionListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        MScoreboardHandler.init(new GameSidebar(this));
    }

    @Override
    public void onDisable() {
        mapManager.save();
    }

    private void registerCommands() {
        SpigotConfig.unknownCommandMessage = Locale.SYSTEM_PREFIX + "Command not found"; // Change our unknown command to valorant's automatically
        Commodore commodore = CommodoreProvider.getCommodore(this);
        try {
            PluginCommand command = getCommand("map");
            command.setExecutor(new MapCommand(this));

            commodore.register(command, CommodoreFileFormat.parse(getResource("map.commodore")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Valorant getInstance() {
        return instance;
    }

}
