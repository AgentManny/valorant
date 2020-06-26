package gg.manny.valorant.sidebar;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.util.scoreboard.ScoreboardAdapter;
import lombok.RequiredArgsConstructor;
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
        Agent selectedAgent = plugin.getAgentManager().getAgent(player);

        lines.add(ChatColor.GRAY + "Pre-lobby");
        lines.add(" ");
        lines.add(ChatColor.WHITE + "Selected Agent:");
        lines.add(selectedAgent == null ? ChatColor.RED + "None" : selectedAgent.getDisplayName());
        lines.add("  ");
        lines.add(ChatColor.WHITE + "Starting in: ");
        lines.add(ChatColor.RED + "1:00");
        lines.add("  ");
        lines.add(ChatColor.RED + "valorant.manny.gg");
    }
}
