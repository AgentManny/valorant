package gg.manny.valorant.ability.sova;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.listeners.ProjectileAbility;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.player.PlayerMeta;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.material.Openable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.util.Optional;

public abstract class Dart extends Ability implements ProjectileAbility {

    private static final String DART_BOUNCE = "DART_POWER";
    private static final int MAX_DART_BOUNCE = 2;

    public Dart(String name, AbilitySkill skill, AbilityPrice price) {
        super(name, skill, price);
    }

    public abstract Color getArrowParticles();

    public abstract void activate(Player player, Projectile arrow);

    @Override
    public boolean activate(Player player, HotbarAction action) {
        GamePlayer gamePlayer = getPlayer(player);
        PlayerMeta meta = gamePlayer.getMeta();
        meta.putIfAbsent(DART_BOUNCE + "_" + getId(), 0);

        int power = meta.getInt(DART_BOUNCE + "_" + getId());
        ActionHandler.ActionType type = action.getType();
        if (type == ActionHandler.ActionType.LEFT_CLICK) {
            int newValue = power < MAX_DART_BOUNCE ? power + 1 : 0;
            meta.put(DART_BOUNCE + "_" + getId(), power = newValue);
        } else if (type == ActionHandler.ActionType.RIGHT_CLICK && action.getClickedBlock().isPresent()) {
            Optional<Block> block = action.getClickedBlock();
            if (block.isPresent() && (block.get() instanceof Openable)) {
                action.setCancelled(false); // Allow interacting
            } else {
            }
        }
        int stayTime = type == ActionHandler.ActionType.UNHOVER ? 5 : 100000;
        player.sendTitle("", getArrowBounces(power), 0, stayTime, stayTime);
        return false;
    }

    @Override
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getInventory().getHeldItemSlot() == getSlot()) {
                Arrow entity = (Arrow) event.getProjectile();
                GamePlayer gamePlayer = getPlayer(player);
                PlayerMeta meta = gamePlayer.getMeta();

                float force = event.getForce();
                int bounce = meta.getInt(DART_BOUNCE + "_" + getId());

                addArrowMeta(player, entity, force, bounce);
            }
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.hasMetadata("Bounce") && entity.hasMetadata(getId())) {
            float force = entity.getMetadata("Force").get(0).asFloat();
            int bounce = entity.getMetadata("Bounce").get(0).asInt();

            float newForce = force / 1.25F;

            if (bounce > 0) {
                Vector arrowVector = entity.getVelocity();

                final double magnitude = Math.sqrt(
                        Math.pow(arrowVector.getX(), 2) +
                                Math.pow(arrowVector.getY(), 2) +
                                Math.pow(arrowVector.getZ(), 2));
                Location hitLoc = entity.getLocation();

                BlockIterator b = new BlockIterator(hitLoc.getWorld(),
                        hitLoc.toVector(), arrowVector, 0, 3);

                Block hitBlock = event.getEntity().getLocation().getBlock();

                Block blockBefore = hitBlock;
                Block nextBlock = b.next();

                while (b.hasNext() && nextBlock.getType() == Material.AIR) {
                    blockBefore = nextBlock;
                    nextBlock = b.next();
                }

                BlockFace blockFace = nextBlock.getFace(blockBefore);

                if (blockFace != null) {
                    Vector hitPlain = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());

                    double dotProduct = arrowVector.dot(hitPlain);
                    Vector u = hitPlain.multiply(dotProduct).multiply(2.0);

                    float speed = (float) magnitude;
                    speed *= newForce;

                    Arrow newArrow = entity.getWorld().spawnArrow(entity.getLocation(), arrowVector.subtract(u), speed, 12.0F);
                    addArrowMeta(entity.getShooter(), newArrow, newForce, --bounce);
                    entity.remove();
                }
            } else if (entity.getShooter() instanceof Player) {
                activate((Player) entity.getShooter(), entity);
            }
        }
    }

    private String getArrowBounces(int value) {
        final char symbol = '\u27D0';
        StringBuilder power = new StringBuilder();
        for (int i = 0; i < MAX_DART_BOUNCE; i++) {
            power.append(i < value ? ChatColor.AQUA : ChatColor.GRAY).append(symbol);
        }
        return power.toString();
    }

    private void addArrowMeta(ProjectileSource shooter, Arrow arrow, float force, int bounce) {
        arrow.setShooter(shooter);
        arrow.setMetadata(getId(), new FixedMetadataValue(Valorant.getInstance(), null));
        if (bounce > 0) {
            arrow.setMetadata("Bounce", new FixedMetadataValue(Valorant.getInstance(), bounce));
        }
        arrow.setMetadata("Force", new FixedMetadataValue(Valorant.getInstance(), force));
        arrow.setColor(getArrowParticles());
    }

}
