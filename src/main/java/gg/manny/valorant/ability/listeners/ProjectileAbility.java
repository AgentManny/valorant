package gg.manny.valorant.ability.listeners;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public interface ProjectileAbility {

    default void onProjectileLaunch(ProjectileLaunchEvent event) {

    }

    default void onProjectileHit(ProjectileHitEvent event) {

    }

    default void onEntityShootBow(EntityShootBowEvent event) {

    }

}
