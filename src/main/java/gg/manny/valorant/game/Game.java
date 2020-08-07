package gg.manny.valorant.game;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.map.GameMap;
import gg.manny.valorant.team.GameTeam;
import gg.manny.valorant.team.TeamManager;
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

import java.util.Map;

@Getter
@Setter
public class Game {

    private final Valorant instance;
    private final TeamManager teamManager;

    private GameMap map;

    private GameState state;
    private GameState roundState;

    private int timer;
    private BukkitTask task;
    private BossBar timerBar;

    private long startingIn = -1;
    private long startedAt = -1;

    private Pair<TeamType, GameTeam> winners;

    public Game(Valorant instance) {
        this.instance = instance;
        this.teamManager = instance.getTeamManager();

        state = GameState.WAITING;
        timerBar = Bukkit.createBossBar(state.getName(), BarColor.RED, BarStyle.SOLID);

        task = instance.getServer().getScheduler().runTaskTimer(instance, this::tick, 20L, 20L);
    }

    public void start() {
        Bukkit.broadcastMessage(Locale.SYSTEM_PREFIX + "Game started");
        state = GameState.STARTED;
        startedAt = System.currentTimeMillis();
        startingIn = -1L;

        for (Map.Entry<TeamType, GameTeam> entry : teamManager.getTeams().entrySet()) {
            TeamType type = entry.getKey();
            GameTeam team = entry.getValue();

        }
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
