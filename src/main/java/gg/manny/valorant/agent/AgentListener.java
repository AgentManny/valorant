package gg.manny.valorant.agent;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class AgentListener implements Listener {

    private final AgentManager agentManager;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        agentManager.getPlayerAgents().remove(player.getUniqueId()); // todo temp
    }

}
