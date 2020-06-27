package gg.manny.valorant.ability.cypher;

import gg.manny.valorant.ability.Ability;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Spycam extends Ability {

    private Set<UUID> hovering = new HashSet<>();

    public Spycam() {
        super("Spycam", AbilitySkill.BASIC, AbilityPrice.FREE);
    }

    @Override
    public Material getIcon() {
        return Material.CAKE;
    }

    @Override
    public String getDescription() {
        return "Equip a spycam. Fire to place the spycam at the targeted location. " +
                "REUSE this ability to take control of the camera's view. While in control of the camera, Fire to shoot a tracking dart. " +
                "The dart is removable and will periodically reveal the location of the enemy hit.";
    }

    @Override
    public int getSlot() {
        return 4;
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        if (action.getType() == ActionHandler.ActionType.HOVER) {

            if (hovering.remove(player.getUniqueId())) {

            } else {
                hovering.add(player.getUniqueId());
            }
        }
        return false;
    }
}
