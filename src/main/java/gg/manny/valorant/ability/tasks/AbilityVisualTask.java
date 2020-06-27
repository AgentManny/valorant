package gg.manny.valorant.ability.tasks;

import gg.manny.valorant.Valorant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AbilityVisualTask extends BukkitRunnable {

    @Override
    public void run() {
        Valorant.getInstance().getAgentManager().getPlayerAgents().forEach((playerId, agent) -> {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                agent.getAbilities().forEach(ability -> ability.tick(player));
            }
        });
    }
}
