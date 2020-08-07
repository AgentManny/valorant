package gg.manny.valorant.ability.sova.darts;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.sova.Dart;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.concurrent.TimeUnit;

public class ReconDart extends Dart {

    private ParticleEffect RECON_PARTICLE = ParticleEffect.REDSTONE;

    public static java.awt.Color RECON_COLOR = java.awt.Color.CYAN;

    public static int RECON_RADIUS = 20;
    public static long RECON_TIME = TimeUnit.SECONDS.toMillis(10);


    public ReconDart() {
        super("Recon Dart", AbilitySkill.SIGNATURE, AbilityPrice.FREE);
    }

    @Override
    public Color getArrowParticles() {
        return Color.ORANGE;
    }

    @Override
    public void activate(Player player, Projectile arrow) {
        new ReconDartTask(player, arrow).runTaskTimer(Valorant.getInstance(), 0L, 1L);
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

    private class ReconDartTask extends BukkitRunnable {

        private final Player shooter;
        private final Projectile arrow;

        private long timestamp = System.currentTimeMillis();

        private World world;
        private double locX, locY, locZ;

        private int yOffset = 10;

        private double particles = 1;

        private ReconDartTask(Player shooter, Projectile arrow) {
            this.shooter = shooter;
            this.arrow = arrow;

            Location location = arrow.getLocation();
            world = location.getWorld();
            locX = location.getX();
            locY = location.getY();
            locZ = location.getZ();
        }


        @Override
        public void run() {
            if (System.currentTimeMillis() - timestamp > RECON_TIME || !arrow.isValid() || arrow.isDead()) {
                arrow.remove();
                cancel();
                return;
            }

            double radius = particles += 0.25;
            int points = (int) (radius * 10);
            for (int i = 0; i < points; i++) {
                if (points % 5 == 0) continue; // Slight optimization to reduce the amount of particles
                if (radius >= RECON_RADIUS) {
                    radius = particles = 1;
                    continue;
                }

                double angle = Math.toRadians((double) i / points * 360d);
                double x = locX + Math.cos(angle) * radius;
                double z = locZ + Math.sin(angle) * radius;

                Location effectLocation = new Location(world, x, locY + 0.1, z);
                if (effectLocation.getBlock().isPassable()) {
                    for (double y = effectLocation.getY() - yOffset; y < effectLocation.getY() + 0.5; y++) {
                        Location location = effectLocation.clone();
                        location.setY(y + 0.1);
                        if (location.getBlock().isPassable()) {
                            RECON_PARTICLE.display(location, RECON_COLOR);
                        }
                    }
                } else {
                    double offset = 0.5;
                    for (int y = effectLocation.getBlockY() - yOffset; y < effectLocation.getBlockY() + yOffset; y++) {
                        Location location = effectLocation.clone();
                        Location difference = location.clone().subtract(locX, locY, locZ);

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
                                RECON_PARTICLE.display(location, java.awt.Color.CYAN);
                            }
                        }
                    }
                }
            }
        }
    }
}
