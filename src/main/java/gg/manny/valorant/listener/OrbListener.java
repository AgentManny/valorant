package gg.manny.valorant.listener;

import gg.manny.valorant.orb.Orb;
import gg.manny.valorant.orb.OrbManager;
import gg.manny.valorant.orb.OrbType;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.ipvp.ingot.HotbarApi;

@RequiredArgsConstructor
public class OrbListener implements Listener {

    private final OrbManager orbManager;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();
        if (player.isOp() && HotbarApi.getCurrentHotbar(player) == null && block.getType() == Material.END_PORTAL_FRAME) {
            event.setCancelled(true);

            if (orbManager.getOrbByLocation(block.getLocation()) != null) {
                player.sendMessage(ChatColor.RED + "There is already an orb located here.");
                return;
            }

            Location location = block.getLocation()
                    .add(0.5, 0, 0.5);
            Orb orb = new Orb(OrbType.ULTIMATE_POINT_ORB, location);
            orb.setActive(true);
            orbManager.getOrbs().add(orb);
        }
    }

}
