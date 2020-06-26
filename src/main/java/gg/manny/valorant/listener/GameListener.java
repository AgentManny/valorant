package gg.manny.valorant.listener;

import gg.manny.valorant.game.Game;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final Game game;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        game.onPlayerConnect(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        game.onPlayerDisconnect(event.getPlayer());
    }


}
