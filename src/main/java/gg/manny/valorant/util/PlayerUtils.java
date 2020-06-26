package gg.manny.valorant.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;

public final class PlayerUtils {

    // Static utility class -- cannot be created.
    private PlayerUtils() {
    }

    public static Player getDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                return (Player) ((Projectile) event.getDamager()).getShooter();
            }
        }

        return null;
    }

    /**
     * Resets a player's inventory (and other associated data, such as health, food, etc) to their default state.
     *
     * @param player The player to reset
     */
    public static void resetInventory(Player player) {
        resetInventory(player, null);
    }

    /**
     * Resets a player's inventory (and other associated data, such as health, food, etc) to their default state.
     *
     * @param player   The player to reset
     * @param gameMode The gamemode to reset the player to. null if their current gamemode should be kept.
     */
    public static void resetInventory(Player player, GameMode gameMode) {
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0.0f);
        player.setFoodLevel(20);
        player.setSaturation(10.0f);

        player.setLevel(0);
        player.setExp(0.0f);

        if (!player.hasMetadata("modmode")) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }
    }

    public static Player getDamageSource(Entity damager) {
        Projectile projectile;
        Player playerDamager = null;
        if (damager instanceof Player) {
            playerDamager = (Player) damager;
        } else if (damager instanceof Projectile && (projectile = (Projectile) damager).getShooter() instanceof Player) {
            playerDamager = (Player) projectile.getShooter();
        }
        return playerDamager;
    }

}

