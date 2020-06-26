package gg.manny.valorant.util.scoreboard;

import org.bukkit.entity.Player;

import java.util.LinkedList;

public interface ScoreboardAdapter {

    String getTitle(Player player);

    void getLines(LinkedList<String> lines, Player player);

}

