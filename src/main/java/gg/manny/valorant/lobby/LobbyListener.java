package gg.manny.valorant.lobby;

import gg.manny.valorant.Valorant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ipvp.ingot.HotbarApi;

public class LobbyListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Valorant.getInstance().getLobbyManager().getTimerBar().addPlayer(player);
        HotbarApi.setCurrentHotbar(player, LobbyManager.SELECT_HOTBAR);
    }

}
