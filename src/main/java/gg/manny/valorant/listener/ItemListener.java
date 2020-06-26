package gg.manny.valorant.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
//        if (event.hasItem() && event.getAction() == Action.RIGHT_CLICK_AIR) {
//            if (event.getItem().isSimilar(AgentManager.AGENT_SELECT_ITEM)) {
//                new SelectAgentMenu().openMenu(player);
//            }
//            if (event.getItem().getType() == Material.BEDROCK) {
//                player.sendMessage(ChatColor.RED + "Force reloading....");
//                Bukkit.reload();
//            }
//        }
    }

}
