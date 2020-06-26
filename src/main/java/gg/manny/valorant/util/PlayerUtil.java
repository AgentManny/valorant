package gg.manny.valorant.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static Player getPlayerByEyeLocation(Player player, double distance) {
        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());

        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(distance));

        Player hit = null;

        // Get nearby entities
        for (Player target : player.getWorld().getPlayers()) {
            // Bounding box of the given player
            Vector3D targetPos = new Vector3D(target.getLocation());
            Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
            Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

            if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (hit == null ||
                        hit.getLocation().distanceSquared(observerPos) >
                                target.getLocation().distanceSquared(observerPos)) {

                    hit = target;
                }
            }
        }

        return hit;
    }

    public static boolean hit(Entity entity, Player target) {
        Location observerPos = entity.getLocation();
        Vector3D targetPos = new Vector3D(target.getLocation());

        // Bounding box of the given player
        Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
        Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);

        Vector3D observerStart = new Vector3D(observerPos);

        return target != entity && hasIntersection(observerStart, targetPos, minimum, maximum) && target.getLocation().distanceSquared(observerPos) >
                entity.getLocation().distanceSquared(observerPos);
    }

    private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        final double epsilon = 0.0001f;

        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();

        if (Math.abs(c.x) > e.x + ad.x)
            return false;
        if (Math.abs(c.y) > e.y + ad.y)
            return false;
        if (Math.abs(c.z) > e.z + ad.z)
            return false;

        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon)
            return false;
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon)
            return false;
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + epsilon)
            return false;

        return true;
    }
}
