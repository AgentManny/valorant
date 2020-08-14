package gg.manny.valorant.orb;

import gg.manny.valorant.Valorant;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class OrbManager {

    @Getter private List<Orb> orbs = new ArrayList<>();

    public OrbManager(Valorant plugin) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> orbs.forEach(orb -> {
            if (orb.isActive()) {
                orb.runTask();
            }
        }), 1L, 1L);
    }

    public void addOrbs() {
        for (Orb orb : orbs) {
            orb.setActive(true);
        }
    }

    public Orb getOrbByLocation(Location location) {
        for (Orb orb : orbs) {
            if (orb.getLocation().equals(location)) {
                return orb;
            }
        }
        return null;
    }
}
