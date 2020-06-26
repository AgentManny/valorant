package gg.manny.valorant.sidebar;

import gg.manny.valorant.util.scoreboard.ScoreboardAdapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class GameSidebar implements ScoreboardAdapter {

    @Override
    public String getTitle(Player player) {
        return ChatColor.DARK_RED.toString() + ChatColor.BOLD + "VALORANT";
    }

    @Override
    public void getLines(LinkedList<String> lines, Player player) {
        lines.add(ChatColor.GRAY + "Lobby (10/24)");
        lines.add(" ");
        lines.add(ChatColor.WHITE + "Selected Agent:");
        lines.add(ChatColor.YELLOW + "Cypher");
        lines.add("  ");
        lines.add(ChatColor.WHITE + "Starting in: ");
        lines.add(ChatColor.RED + "1:00");
        lines.add("  ");
        lines.add(ChatColor.RED + "valorant.manny.gg");
    }
}
