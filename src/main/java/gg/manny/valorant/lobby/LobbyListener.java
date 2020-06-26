package gg.manny.valorant.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ipvp.ingot.HotbarApi;

public class LobbyListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HotbarApi.setCurrentHotbar(player, LobbyManager.SELECT_HOTBAR);
    }

}
