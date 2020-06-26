package gg.manny.valorant.ability.sage;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

public class SlowOrb extends Ability {

    public SlowOrb() {
        super("Slow Orb", AbilityType.BASIC, ChatColor.AQUA);
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public Material getIcon() {
        return Material.BLUE_ICE;
    }

    @Override
    public String getDescription() {
        return "Equip a Slowing Orb. " +
                "Fire to launch the Orb, which expands upon hitting the ground, creating a zone that slows players who walk on it.";
    }

    @Override
    public int getSlot() {
        return 4;
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        // todo Check if they have enough money to buy or if they purchased any for game starting
        if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK) {
            Snowball projectile = player.launchProjectile(Snowball.class);
            projectile.setMetadata(getName(), new FixedMetadataValue(Valorant.getInstance(), player.getUniqueId().toString())); // Store UUID for faster access we could also use Projectile#getShooter
            return true;
        }
        return false;
    }
}
