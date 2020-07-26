package gg.manny.valorant.listener;

import gg.manny.valorant.Valorant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AbilityListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Valorant.getInstance().getAgentManager().getAgents().forEach(agent -> {
            agent.getAbilities().forEach(ability -> ability.onProjectileHit(event));
        });
    }

}
