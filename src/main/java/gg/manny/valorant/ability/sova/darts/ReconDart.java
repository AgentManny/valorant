package gg.manny.valorant.ability.sova.darts;

import gg.manny.valorant.ability.sova.Dart;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class ReconDart extends Dart {

    public ReconDart() {
        super("Recon Dart", AbilitySkill.SIGNATURE, AbilityPrice.FREE);
    }

    @Override
    public Color getArrowParticles() {
        return Color.ORANGE;
    }

    @Override
    public void activate(Player player, Projectile arrow) {
        Location location = arrow.getLocation();
        arrow.playEffect(EntityEffect.HURT_EXPLOSION);
        player.sendMessage("Explosion!");
    }

    @Override
    public int getCooldown() {
        return 3;
    }

    @Override
    public Material getIcon() {
        return Material.BOW;
    }

    @Override
    public String getDescription() {
        return "Fire a bolt that deploys a sonar emitter. The sonar pings tag nearby enemies, causing them to be revealed. Can be destroyed.";
    }
}
