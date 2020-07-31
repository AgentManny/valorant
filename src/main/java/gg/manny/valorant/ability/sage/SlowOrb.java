package gg.manny.valorant.ability.sage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.listeners.ProjectileAbility;
import gg.manny.valorant.util.MathUtil;
import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.Vector3f;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftArmorStand;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;
import xyz.xenondevs.particle.ParticleEffect;
import xyz.xenondevs.particle.data.texture.BlockTexture;

import java.util.UUID;

public class SlowOrb extends Ability implements ProjectileAbility {

    private static final int COOLDOWN = 5;
    private static final int RADIUS = 3;

    private static final String SLOW_ORB_METADATA = "SLOW_ORB";
    private static final double SLOW_ORB_SPEED = 0.35;

    private Multimap<UUID, Integer> dummyEntities = ArrayListMultimap.create();

    public SlowOrb() {
        super("Slow Orb", AbilitySkill.BASIC, AbilityPrice.CREDITS);
    }

    @Override
    public int getCost() {
        return 100;
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

    // Configureable till we find a better sound
    public static float volume = 3f;
    public static float pitch = 1.2f;

    private long lastTick = -1;
    @Override
    public void tick(Player player) {
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BLUE_ICE) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 3));
            if (lastTick < System.currentTimeMillis()) { // Prevent ur ears from exploding
                if (player.isSprinting()) {
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, volume, pitch);
                    lastTick = System.currentTimeMillis() + 500L;
                }
            }
        }
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        if (action == null) { // Hacky way of just adding
        } else if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK) {
            Snowball projectile = player.launchProjectile(Snowball.class);

            SlowOrbVisual slowOrbVisual = new SlowOrbVisual();
            slowOrbVisual.entity = projectile;
            slowOrbVisual.runTaskTimer(Valorant.getInstance(), 2L, 2L);

            projectile.setMetadata(SLOW_ORB_METADATA, new FixedMetadataValue(Valorant.getInstance(), slowOrbVisual)); // Store UUID for faster access we could also use Projectile#getShooter
            return true;
        }
        return false;
    }

    public void activate(Player player, Entity entity) {
        Location location = entity.getLocation();
        SlowOrbVisual value = (SlowOrbVisual) entity.getMetadata(SLOW_ORB_METADATA).get(0).value();
        value.cancel();


        if (location.getBlock().getType() == Material.AIR) {
            location.subtract(0, 1, 0);
        }

        int cx = location.getBlockX();
        int cy = location.getBlockY();
        int cz = location.getBlockZ();

        int rSquared = RADIUS * RADIUS;
        for (int x = cx - RADIUS; x <= cx +RADIUS; x++) {
            for (int z = cz - RADIUS; z <= cz + RADIUS; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {

                    Block block = location.getWorld().getBlockAt(x, cy, z);
                    Material oldMaterial = block.getType();
                    Valorant.getInstance().getServer().getScheduler().runTaskLater(Valorant.getInstance(), () -> {
                        block.setType(oldMaterial);

                    }, (20L * COOLDOWN) + (long) MathUtil.randomDouble(3, 10));
                    if (oldMaterial != Material.AIR) {
                        block.setType(Material.BLUE_ICE);
                    }
                }
            }
        }

        for (int i = 0; i < (RADIUS * 15); i++) {
            ArmorStand armorStand = createEntity(location);
            Valorant.getInstance().getServer().getScheduler().runTaskLater(Valorant.getInstance(), armorStand::remove, (20L * COOLDOWN) + (long)MathUtil.randomDouble(5, 20));
        }
    }


    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity instanceof Snowball && entity.hasMetadata(SLOW_ORB_METADATA)) {
            boolean floor = event.getHitEntity() != null || event.getHitBlockFace() == BlockFace.UP;
            if (!floor) {
                Vector velocity = entity.getVelocity().clone();
                velocity.setY(-velocity.getY());
                entity.setVelocity(velocity);

                Vector v = entity.getVelocity().clone();
                Vector v2 = entity.getLocation().toVector().normalize();

                Vector output = v.subtract(v2.multiply(v.dot(v2))).multiply(-SLOW_ORB_SPEED);

                Snowball newEntity = entity.getWorld().spawn(event.getHitEntity() != null ? event.getHitEntity().getLocation() : entity.getLocation(), Snowball.class);
                newEntity.setShooter(entity.getShooter());

                SlowOrbVisual value = (SlowOrbVisual) entity.getMetadata(SLOW_ORB_METADATA).get(0).value();
                value.entity = newEntity;

                newEntity.setMetadata(SLOW_ORB_METADATA, new FixedMetadataValue(Valorant.getInstance(), value));
                newEntity.setVelocity(output);
            } else {
                activate((Player) entity.getShooter(), entity);
            }
        }
    }

    private ArmorStand createEntity(Location centerLoc) {
        double spread = RADIUS - 0.5;

        double newX = centerLoc.getX() + MathUtil.randomDouble(-spread, spread);
        double newY = (centerLoc.getY() + 0.4) + MathUtil.randomDouble(0, .5) - 0.75;
        double newZ = centerLoc.getZ() + MathUtil.randomDouble(-spread, spread);

        ArmorStand entity = centerLoc.getWorld().spawn(new Location(centerLoc.getWorld(), newX, newY, newZ, 0, 0), ArmorStand.class);
        EntityArmorStand handle = ((CraftArmorStand) entity).getHandle();
        entity.setCollidable(false); // Prevent other abilities colliding with it
        handle.setInvisible(true);
        handle.setFlag(5, true); // Invisibility
        handle.setHeadPose(new Vector3f((float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360))));

        entity.setHelmet(new ItemStack(Valorant.RANDOM.nextBoolean() ? Material.ICE : Valorant.RANDOM.nextBoolean() ? Material.BLUE_ICE : Material.PACKED_ICE));
        entity.setSmall(true);
        entity.setGravity(false);
        return entity;
    }

    private class SlowOrbVisual extends BukkitRunnable {

        public Entity entity;

        @Override
        public void run() {
            if (entity == null || entity.isDead()) {
                cancel();
                return;
            }

            ParticleEffect.BLOCK_CRACK.display(entity.getLocation(), 0f, 0f, 0f, 0f, 3, new BlockTexture(Material.BLUE_ICE), Bukkit.getOnlinePlayers());
        }
    }
}
