package gg.manny.valorant.ability.cypher;

import gg.manny.valorant.ability.Ability;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ipvp.ingot.HotbarAction;

public class NeuralTheft extends Ability {

    public NeuralTheft() {
        super("Neural Theft", AbilitySkill.ULTIMATE, AbilityPrice.ULTIMATE_POINTS);
    }


    @Override
    public Material getIcon() {
        return Material.ENDER_EYE;
    }

    @Override
    public String getDescription() {
        return "Extract information from the corpse of an enemy, revealing the location of their living allies.";
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        player.getNearbyEntities(20, 20, 20).forEach(entity -> {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
                //play sound to those entities once they get the effect
            }
        });
        player.sendTitle("", ChatColor.RED.toString() + ChatColor.BOLD + "LOCATION REVEALED", 10, 60, 10);
//
//        for (Player online : Bukkit.getOnlinePlayers()) {
//            online.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 60, 0));
//        }
        return true;
    }
}
