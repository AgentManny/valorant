package gg.manny.valorant.listener;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.listeners.ProjectileAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class AbilityListener implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Valorant.getInstance().getAgentManager().getAgents().forEach(agent -> {
            for (Ability ability : agent.getAbilities()) {
                if (ability instanceof ProjectileAbility) {
                    ((ProjectileAbility) ability).onProjectileLaunch(event);
                }
            }
        });
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Valorant.getInstance().getAgentManager().getAgents().forEach(agent -> {
            for (Ability ability : agent.getAbilities()) {
                if (ability instanceof ProjectileAbility) {
                    ((ProjectileAbility) ability).onProjectileHit(event);
                }
            }
        });
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Valorant.getInstance().getAgentManager().getAgents().forEach(agent -> {
                for (Ability ability : agent.getAbilities()) {
                    if (ability instanceof ProjectileAbility) {
                        ((ProjectileAbility) ability).onEntityShootBow(event);
                    }
                }
            });
        }
    }

}
