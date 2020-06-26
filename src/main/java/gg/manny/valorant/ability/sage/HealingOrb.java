package gg.manny.valorant.ability.sage;

import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.util.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

public class HealingOrb extends Ability {

    public HealingOrb() {
        super("Healing Orb", ChatColor.AQUA);
    }

    @Override
    public Material getIcon() {
        return Material.HEART_OF_THE_SEA;
    }

    @Override
    public String getDescription() {
        return "EQUIP a healing orb. " +
                "Left Click with your crosshairs over a damaged ally to activate a heal-over-time on them. " +
                "Right Click while Sage is damaged to activate a self heal-over-time.";
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public int getSlot() {
        return 3;
    }

    @Override
    public void tick(Player player) {
        //Player lookingAt = PlayerUtil.getPlayerByEyeLocation(player, 12);
        Player lookingAt = player;
        if (lookingAt != null) {
            Location playerLoc = player.getLocation().clone();
            int radius = 2;
            for (double y = 0; y <= player.getLocation().getBlockY() + 2; y += 0.05) {
                double x = radius * Math.cos(y);
                double z = radius * Math.sin(y);
                ParticleEffect.ENCHANTMENT_TABLE.send(playerLoc.add(x, 0, z), 1, 2);
            }
        }
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK_ENTITY) {
            player.sendMessage(ChatColor.GREEN + "Healing player...");
            // Run task
            // Now add cooldown
            return true; // This adds cooldown. so cool
        } else if (action.getType() == ActionHandler.ActionType.LEFT_CLICK) {
            return true;
        }
        return false;
    }
}
