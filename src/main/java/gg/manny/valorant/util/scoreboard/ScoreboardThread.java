package gg.manny.valorant.util.scoreboard;

import gg.manny.valorant.Valorant;
import org.bukkit.entity.Player;

final class ScoreboardThread extends Thread {

    public ScoreboardThread() {
        super("Valorant - Scoreboard Thread");
        setDaemon(false);
    }

    public void run() {
        while (true) {
            for (Player online : Valorant.getInstance().getServer().getOnlinePlayers()) {
                try {
                    MScoreboardHandler.updateScoreboard(online);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(MScoreboardHandler.getUpdateInterval() * 50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}