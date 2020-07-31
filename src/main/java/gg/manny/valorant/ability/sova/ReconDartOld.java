package gg.manny.valorant.ability.sova;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.listeners.ProjectileAbility;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.player.PlayerMeta;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.util.concurrent.TimeUnit;

public class ReconDartOld extends Ability implements ProjectileAbility {

    private static final long RECON_TIME = TimeUnit.SECONDS.toMillis(5);
    private static final String RECON_POWER = "RECON_POWER";
    private static final int MAX_RECON_BOUNCE = 2;

    public ReconDartOld() {
        super("Recon Dart", AbilitySkill.SIGNATURE, AbilityPrice.FREE);
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

    @Override
    public boolean activate(Player player, HotbarAction action) {
        GamePlayer gamePlayer = getPlayer(player);
        PlayerMeta meta = gamePlayer.getMeta();
        meta.putIfAbsent(RECON_POWER, 0);

        int power = meta.getInt(RECON_POWER);
        ActionHandler.ActionType type = action.getType();
        if (type == ActionHandler.ActionType.LEFT_CLICK) {
            int newValue = power < MAX_RECON_BOUNCE ? power + 1 : 0;
            meta.put(RECON_POWER, power = newValue);
        } else if (type == ActionHandler.ActionType.RIGHT_CLICK) {
            action.setCancelled(false); // Allow interacting
        }

        int stayTime = type == ActionHandler.ActionType.UNHOVER ? 5 : 100000;
        player.sendTitle("", getPower(power), 0, stayTime, stayTime);
        return false;
    }

    private String getPower(int value) {
        final char symbol = '\u27D0';
        StringBuilder power = new StringBuilder();
        for (int i = 0; i < MAX_RECON_BOUNCE; i++) {
            power.append(i < value ? ChatColor.AQUA : ChatColor.GRAY).append(symbol);
        }
        return power.toString();
    }

    public void activate(Player player, Entity projectile) {
    }

    @Override
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Arrow entity = (Arrow) event.getProjectile();
            GamePlayer gamePlayer = getPlayer(player);
            PlayerMeta meta = gamePlayer.getMeta();

            float force = event.getForce();
            int bounce = meta.getInt(RECON_POWER);

            entity.setMetadata("Force", new FixedMetadataValue(Valorant.getInstance(), force));
            if (bounce > 0) {
                entity.setMetadata("Bounce", new FixedMetadataValue(Valorant.getInstance(), bounce));
            }
            // meta.put(PLAYER_RECON_POWER, 0);
            entity.setColor(org.bukkit.Color.BLUE);
        }
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.hasMetadata("Bounce")) {
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
                    newArrow.setShooter(entity.getShooter());
                    newArrow.setMetadata("Force", new FixedMetadataValue(Valorant.getInstance(), newForce));
                    newArrow.setColor(org.bukkit.Color.BLUE);

                    if (--bounce > 0) {
                        newArrow.setMetadata("Bounce", new FixedMetadataValue(Valorant.getInstance(), bounce));
                    }
                    entity.remove();
                }
            } else if (entity.getShooter() instanceof Player) {
                activate((Player) entity.getShooter(), entity);
            }
        }
    }
}
