package gg.manny.valorant;

import gg.manny.valorant.agent.AgentManager;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.listener.GameListener;
import gg.manny.valorant.listener.ItemListener;
import gg.manny.valorant.listener.PlayerListener;
import gg.manny.valorant.lobby.LobbyManager;
import gg.manny.valorant.player.PlayerManager;
import gg.manny.valorant.sidebar.GameSidebar;
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

    private PlayerManager playerManager;
    private AgentManager agentManager;

    // todo remove lobby shit
    private LobbyManager lobbyManager;


    @Override
    public void onEnable() {
        instance = this;

        game = new Game(this);

        playerManager = new PlayerManager(this);
        agentManager = new AgentManager(this);

        lobbyManager = new LobbyManager(this); // todo remove

        Arrays.asList(
                new GameListener(game),
                new PlayerListener(this),
                new ItemListener(),
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
