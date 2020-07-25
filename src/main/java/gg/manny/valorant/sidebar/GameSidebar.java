package gg.manny.valorant.sidebar;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.game.GameState;
import gg.manny.valorant.game.TeamType;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.util.TimeUtils;
import gg.manny.valorant.util.scoreboard.ScoreboardAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Map;

@RequiredArgsConstructor
public class GameSidebar implements ScoreboardAdapter {

    private final Valorant plugin;

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "VALORANT";
    }

    @Override
    public void getLines(LinkedList<String> lines, Player player) {
        Game game = plugin.getGame();
        GamePlayer gamePlayer = plugin.getPlayerManager().getByPlayer(player);
        if (gamePlayer == null) {
            lines.add(ChatColor.RED + "Your data isn't loaded yet.");
            lines.add(ChatColor.RED + "Please contact a staff member,");
            lines.add(ChatColor.RED + "if you believe this is an error.");
            return;
        }

        GameLobby lobby = plugin.getLobby();

        TeamType team = gamePlayer.getTeam();
        Agent selectedAgent = plugin.getAgentManager().getAgent(player);

        lines.add(ChatColor.GRAY + game.getState().getFriendlyName());
        lines.add(" ");

        if (game.getState() == GameState.WAITING || game.getState() == GameState.STARTING) {
            lines.add(ChatColor.WHITE + "Players: " + ChatColor.RED + Bukkit.getOnlinePlayers().size() + "/" + GameLobby.REQUIRED_PLAYERS);
            if (game.getState() == GameState.STARTING) {
                lines.add(ChatColor.WHITE + "Starting in: " + ChatColor.RED + TimeUtils.formatIntoMMSS(game.getTimer()));
            }
            lines.add("  ");
            // When a map has been decided (only if required players
            // lines.add(ChatColor.WHITE + "Chosen map:");
            lines.add(ChatColor.WHITE + "Map Votes:");
            int i = 0;
            for (Map.Entry<String, Integer> entry : lobby.getVotes().entrySet()) {
                lines.add("  " + ChatColor.WHITE + entry.getKey() + ": " + ChatColor.RED + entry.getValue());
                if (i++ == 5) break;
            }
        } else {
            lines.add(ChatColor.WHITE + "Team: " + team.getColor() + team.getName());
            if (team != TeamType.NONE) {
                lines.add(ChatColor.WHITE + "Selected Agent:");
                lines.add(selectedAgent == null ? ChatColor.RED + "None" : selectedAgent.getDisplayName());
            }
            lines.add("  ");
        }

        // Footer/Header
        lines.add("  ");
        lines.add(ChatColor.RED + "valorant.manny.gg");
    }
}
