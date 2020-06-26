package gg.manny.valorant.game;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.player.GameTeam;
import gg.manny.valorant.util.MathUtil;
import gg.manny.valorant.util.TimeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Game {

    public static final int STARTING_TIMER = 20;
    public static final int GAME_TIMER = 100;

    public static final int REQUIRED_PLAYERS = 1;

    private final Map<Team, GameTeam<GamePlayer>> players = new HashMap<>();

    private GameState state;
    private GameState roundState;

    private int timer;
    private BossBar timerBar;
    private BukkitTask task;

    private long startingIn = -1;
    private long startedAt = -1;

    private Team winners;

    public Game(Valorant instance) {
        for (Team team : Team.values()) {
            players.put(team, new GameTeam<>(team));
        }

        init();
        instance.getServer().getScheduler().runTaskTimer(instance, this::tick, 20L, 20L);
    }

    public void tick() {
        if (--timer < 0) return;

        timerBar.setTitle(state.getName() + ": " + ChatColor.RED + TimeUtils.formatIntoMMSS(timer));

        if (state == GameState.STARTING) {
            timerBar.setProgress(MathUtil.getPercent(timer, STARTING_TIMER) * 0.01F);
        }
    }

    public void init() {
        state = GameState.WAITING;
        timerBar = Bukkit.createBossBar(state.getName(), BarColor.RED, BarStyle.SOLID);
    }

    public void start() {
        state = GameState.STARTING;
        timer = STARTING_TIMER;
    }

    private boolean isPlaying(Player player) {
        for (Map.Entry<Team, GameTeam<GamePlayer>> entry : this.players.entrySet()) {
            Team team = entry.getKey();
            if (team == Team.SPECTATORS) continue;

            GameTeam<GamePlayer> players = entry.getValue();
            if (players.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public Team getPlayerTeam(Player player) {
        for (Map.Entry<Team, GameTeam<GamePlayer>> entry : this.players.entrySet()) {
            if (entry.getValue().containsPlayer(player)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void checkPlayerRequirements() {
        boolean requiredPlayers = REQUIRED_PLAYERS <= Bukkit.getOnlinePlayers().size();
        if (requiredPlayers) {
            if (state == GameState.WAITING) {
                start();
                Bukkit.broadcastMessage(Locale.BROADCAST_PREFIX + "Game starting in " + ChatColor.LIGHT_PURPLE + timer + " seconds" + ChatColor.WHITE + ".");
            }
        } else if (state == GameState.STARTING) {
            timer = -1;
            state = GameState.WAITING;
            Bukkit.broadcastMessage(Locale.BROADCAST_PREFIX + "Game delayed, there isn't enough players.");
        }
    }

    public void onPlayerConnect(Player player) {
        Bukkit.broadcastMessage(Locale.SYSTEM_PREFIX + player.getName() + " has " + (isPlaying(player) && state != GameState.WAITING ? "re" : "") + "connected.");
        timerBar.addPlayer(player);
        checkPlayerRequirements();
    }

    public void onPlayerDisconnect(Player player) {
        Bukkit.broadcastMessage(Locale.SYSTEM_PREFIX + player.getName() + " has disconnected.");
        timerBar.removePlayer(player);
        checkPlayerRequirements();
    }



}
