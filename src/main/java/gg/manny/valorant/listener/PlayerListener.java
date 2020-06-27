package gg.manny.valorant.listener;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.player.GamePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final Valorant plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = plugin.getPlayerManager().create(player.getUniqueId(), player.getName(), false);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // todo remove player if they are spectating -- as we're allowing people to relog
        // make sure player spectates teammates
        // todo add a forfeit if they don't come back within 5 minutes
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(ChatColor.DARK_AQUA + "(All)debug " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

}
