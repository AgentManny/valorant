package gg.manny.valorant;

import gg.manny.valorant.agent.AgentListener;
import gg.manny.valorant.agent.AgentManager;
import gg.manny.valorant.listener.ItemListener;
import gg.manny.valorant.listener.PlayerListener;
import gg.manny.valorant.lobby.LobbyManager;
import gg.manny.valorant.sidebar.GameSidebar;
import gg.manny.valorant.util.scoreboard.MScoreboardHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.HotbarFunctionListener;

import java.util.Arrays;

@Getter
public class Valorant extends JavaPlugin {
    
    private static Valorant instance;

    private AgentManager agentManager;

    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        instance = this;

        agentManager = new AgentManager();

        lobbyManager = new LobbyManager(this);

        Arrays.asList(
                new AgentListener(agentManager),
                new PlayerListener(),
                new ItemListener(),
                new HotbarFunctionListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        MScoreboardHandler.init(new GameSidebar());
    }

    @Override
    public void onDisable() {

    }

    public static Valorant getInstance() {
        return instance;
    }
}
