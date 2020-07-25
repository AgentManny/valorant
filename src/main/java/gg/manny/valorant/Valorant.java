package gg.manny.valorant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import gg.manny.valorant.agent.AgentManager;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.listener.PlayerListener;
import gg.manny.valorant.map.MapManager;
import gg.manny.valorant.player.PlayerManager;
import gg.manny.valorant.sidebar.GameSidebar;
import gg.manny.valorant.team.TeamManager;
import gg.manny.valorant.util.scoreboard.MScoreboardHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.ingot.HotbarFunctionListener;

import java.util.Arrays;
import java.util.Random;

@Getter
public class Valorant extends JavaPlugin {

    public static final Random RANDOM = new Random();
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Polygonal2DRegion.class, new TypeToken<Polygonal2DRegion>(){}.getType())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    private static Valorant instance;

    private MapManager mapManager;
    private AgentManager agentManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    private Game game;
    private GameLobby lobby;

    @Override
    public void onEnable() {
        instance = this;

        mapManager = new MapManager(this);

        agentManager = new AgentManager(this);

        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);

        game = new Game(this);
        lobby = new GameLobby(this);

        Arrays.asList(
                new PlayerListener(this),
                new HotbarFunctionListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        MScoreboardHandler.init(new GameSidebar(this));
    }

    @Override
    public void onDisable() {

    }

    public static Valorant getInstance() {
        return instance;
    }

}
