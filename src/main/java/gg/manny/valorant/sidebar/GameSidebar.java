package gg.manny.valorant.sidebar;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.game.Game;
import gg.manny.valorant.game.GameState;
import gg.manny.valorant.game.Team;
import gg.manny.valorant.util.TimeUtils;
import gg.manny.valorant.util.scoreboard.ScoreboardAdapter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;

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
        Team team = game.getPlayerTeam(player);
        Agent selectedAgent = plugin.getAgentManager().getAgent(player);

        lines.add(ChatColor.GRAY + game.getState().getFriendlyName());
        lines.add(" ");
        lines.add(ChatColor.WHITE + "Team: " + (team == null ? ChatColor.RED + "None" : team.getColor() + team.getName()));
        if (team != null) {
            lines.add(ChatColor.WHITE + "Selected Agent:");
            lines.add(selectedAgent == null ? ChatColor.RED + "None" : selectedAgent.getDisplayName());
        }
        lines.add("  ");
        if (game.getState() == GameState.WAITING) {
            lines.add(ChatColor.WHITE + "Players: ");
            lines.add(ChatColor.RED.toString() + Bukkit.getOnlinePlayers().size() + "/" + Game.REQUIRED_PLAYERS);
        } else if (game.getState() == GameState.STARTING) {
            lines.add(ChatColor.WHITE + "Starting in: ");
            lines.add(ChatColor.RED + TimeUtils.formatIntoMMSS(game.getTimer()));
        }
        lines.add("  ");
        lines.add(ChatColor.RED + "valorant.manny.gg");
    }
}
