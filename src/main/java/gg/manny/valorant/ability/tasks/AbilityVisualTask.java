package gg.manny.valorant.ability.tasks;

import gg.manny.valorant.Valorant;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class AbilityVisualTask extends BukkitRunnable {

    private final Valorant plugin;

    @Override
    public void run() {
        plugin.getAgentManager().getPlayerAgents().forEach((playerId, agent) -> {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                agent.getAbilities().forEach(ability -> ability.tick(player));
            }
        });
    }
}
