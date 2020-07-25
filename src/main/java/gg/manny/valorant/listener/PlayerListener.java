package gg.manny.valorant.listener;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.game.GameState;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.team.GameTeam;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ipvp.ingot.HotbarApi;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final Valorant plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Game game = plugin.getGame();
        GamePlayer gamePlayer = plugin.getPlayerManager().create(player.getUniqueId(), player.getName(), false);
        GameTeam team = plugin.getTeamManager().getTeam(gamePlayer.getTeam());

        String message = Locale.SYSTEM_PREFIX + player.getName() + " has " + (team != null && game.getState() != GameState.WAITING ? "re" : "") + "connected.";
        if (team != null) {
            team.broadcast(message);
        } else {
            Bukkit.broadcastMessage(message);
        }

        plugin.getGame().getTimerBar().addPlayer(player);
        plugin.getLobby().checkPlayerRequirements();

        HotbarApi.setCurrentHotbar(player, Locale.GAME_LOBBY_HOTBAR);

        player.sendTitle(ChatColor.RED + "VALORANT","" , 10, 30, 10);
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getGame().getTimerBar().removePlayer(player);
        plugin.getLobby().checkPlayerRequirements();
        plugin.getLobby().setVote(player, null);

        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(ChatColor.DARK_AQUA + "(All) " + player.getName() + ChatColor.WHITE + ": " + event.getMessage());
    }

}
