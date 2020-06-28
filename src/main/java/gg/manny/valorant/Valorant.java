package gg.manny.valorant;

import gg.manny.valorant.agent.AgentManager;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.listener.PlayerListener;
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

    private static Valorant instance;

    private Game game;
    private GameLobby lobby;

    private AgentManager agentManager;

    private TeamManager teamManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;

        game = new Game(this);
        lobby = new GameLobby(this);

        agentManager = new AgentManager(this);

        teamManager = new TeamManager(this);
        playerManager = new PlayerManager(this);

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
