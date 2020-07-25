package gg.manny.valorant.game;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.team.GameTeam;
import gg.manny.valorant.util.MathUtil;
import gg.manny.valorant.util.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
public class Game {

    private GameState state;
    private GameState roundState;

    private int timer;
    private BukkitTask task;
    private BossBar timerBar;

    private long startingIn = -1;
    private long startedAt = -1;

    private Pair<TeamType, GameTeam> winners;

    public Game(Valorant instance) {
        state = GameState.WAITING;
        timerBar = Bukkit.createBossBar(state.getName(), BarColor.RED, BarStyle.SOLID);

        task = instance.getServer().getScheduler().runTaskTimer(instance, this::tick, 20L, 20L);
    }

    public void tick() {
        if (timer <= 0) return;
        --timer;

        if (state == GameState.STARTING) {

        }
        timerBar.setTitle(state.getName() + ": " + ChatColor.RED + TimeUtils.formatIntoMMSS(timer));

        if (state == GameState.STARTING) {
            timerBar.setProgress(MathUtil.getPercent(timer, GameLobby.STARTING_TIMER) * 0.01F);
        }
    }
}
