package gg.manny.valorant.listener;

import gg.manny.valorant.Valorant;
import joptsimple.internal.Strings;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class TestListener implements Listener {

    public static double SPEED_REDUCER = 0.35;
    public static BukkitTask ACTIVE_TASK;

    public static ParticleEffect RECON_PARTICLE = ParticleEffect.REDSTONE;
    public static Color RECON_COLOR = Color.ORANGE;
    public static int RECON_RADIUS = 25;
    public static long RECON_TIME = TimeUnit.SECONDS.toMillis(5);

    /*
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            event.getPlayer().launchProjectile(Arrow.class);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity instanceof Egg) {
            boolean floor = event.getHitEntity() != null || event.getHitBlockFace() == BlockFace.UP; // Radius changes if floor - WALL inital shape is a rectangle
            int bounces = 0;

            if (floor) {

                final double[] count = {1};


                Location centerLoc = entity.getLocation();
                RayTraceResult rayTraceResult = centerLoc.getWorld().rayTraceEntities(centerLoc, centerLoc.getDirection(), RECON_RADIUS, RECON_RADIUS * 10);
                Entity hitEntity = rayTraceResult.getHitEntity();
                if (hitEntity != null && entity instanceof Player) {
                    hitEntity.sendMessage(ChatColor.RED + "You were revealed by a Recon Dart prolly");
                }

                World world = centerLoc.getWorld();
                double locX = centerLoc.getX();
                double locY = centerLoc.getY();
                double locZ = centerLoc.getZ();

                debug("Recon", "Started");

                ACTIVE_TASK = new BukkitRunnable() {

                    long timestamp = System.currentTimeMillis();
                    int particlesFloor = 0;
                    int particleHeight = 0;
                    int particleHeightAir = 0;
                    boolean detectedPlayer = false;

                    @Override
                    public void run() {
                        if (System.currentTimeMillis() - timestamp > RECON_TIME) {
                            cancel();
                            debug("Recon", "Completed", "X Particles: " + particlesFloor, "Y Particles" + particleHeight + " [Excluded: " + (particleHeightAir - particleHeight) + "]", "Total: " + (particlesFloor + particleHeight));
                            return;
                        }

                        double radius = count[0] += 0.25;
                        int points = (int) (radius * 10);
                        for (int i = 0; i < points; i++) {
                            if (points % 5 == 0) continue;
                            if (radius >= RECON_RADIUS) {
                                radius = count[0] = 1;
                                detectedPlayer = false;
                                continue;
                            }

                            Player player = Bukkit.getPlayer("Mannys");
                            if (player != null && player.isSneaking()) {
                                faceDirection(player, centerLoc);
                            }

                            double angle = Math.toRadians((double) i / points * 360d);
                            double x = locX + Math.cos(angle) * radius;
                            double z = locZ + Math.sin(angle) * radius;

                            Location effectLocation = new Location(world, x, locY + 0.1, z);
                            if (effectLocation.getBlock().isPassable()) {
                                RECON_PARTICLE.display(effectLocation, detectedPlayer ? Color.RED : RECON_COLOR);
                                particlesFloor++;

                            } else {
                                int yDiff = 10;
                                double offset = 0.5;
                                for (int y = effectLocation.getBlockY() - yDiff; y < effectLocation.getBlockY() + yDiff; y++) {
                                    Location location = effectLocation.clone();
                                    Location difference = location.clone().subtract(centerLoc);

                                    double diffX = difference.getX();
                                    double diffZ = difference.getZ();

                                    location.setY(y);
                                    if (location.getBlock().getType() != Material.AIR) { // Only adds an offset if location is a block
                                        if (Math.abs(diffX) > Math.abs(diffZ)) {
                                            if (diffX > 0) {
                                                location.add(-offset, 0, 0);
                                            } else {
                                                location.add(offset, 0, 0);
                                            }
                                        } else {
                                            if (diffZ > 0) {
                                                location.add(0, 0, -offset);
                                            } else {
                                                location.add(0, 0, offset);
                                            }
                                        }
                                        if (location.getBlock().getType() == Material.AIR) { // Don't send a particle if the new location isn't a block
                                            RECON_PARTICLE.display(location, Color.CYAN);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.runTaskTimer(Valorant.getInstance(), 0, 1);
            } else {
                debug("Recon", "Wall");
            }
        }
    }

    public void faceDirection(Player player, Location target) {
        Vector dir = target.clone().subtract(player.getEyeLocation()).toVector();
        Location loc = player.getLocation().setDirection(dir);
        player.teleport(loc);
    }

    public static void debug(String key, String... messages) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[Debug] [" + key + "]: (" + Strings.join(messages, ") (") + ")");
    }
     */
}