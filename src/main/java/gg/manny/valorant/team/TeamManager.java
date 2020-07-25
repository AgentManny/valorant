package gg.manny.valorant.team;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.game.TeamType;
import gg.manny.valorant.player.GamePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private final Valorant plugin;

    private GameTeam<GamePlayer> teamOne = new GameTeam<>();
    private GameTeam<GamePlayer> teamTwo = new GameTeam<>();
    private GameTeam<GamePlayer> teamSpectator = new GameTeam<>();

    private Map<TeamType, GameTeam<GamePlayer>> players = new HashMap<>();

    public TeamManager(Valorant plugin) {
        this.plugin = plugin;

        boolean randomTeam = Valorant.RANDOM.nextBoolean();
        players.put(TeamType.ATTACKERS, randomTeam ? teamOne : teamTwo);
        players.put(TeamType.DEFENDERS, randomTeam ? teamTwo : teamOne);
        players.put(TeamType.SPECTATORS, teamSpectator);
    }

    public void initTeam(Scoreboard scoreboard) {
        for (TeamType teamType : TeamType.values()) {
            if (teamType == TeamType.NONE) continue;
            Team team = scoreboard.registerNewTeam(teamType.getName());
            team.setDisplayName(team.getDisplayName());
            team.setPrefix(team.getColor().toString());
            System.out.println("Registered: " + team.getName());
        }
    }

    public boolean containsPlayer(Player player) {
        for (GameTeam<GamePlayer> team : players.values()) {
            if (team.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public void setTeam(TeamType team, Player player) {
        GamePlayer gamePlayer = plugin.getPlayerManager().getByPlayer(player);

        TeamType oldTeam = gamePlayer.getTeam();
        if (oldTeam.getTeam() != null) {
            oldTeam.getTeam().removePlayer(gamePlayer);
        }

        Team sbTeam = player.getScoreboard().getTeam(oldTeam.getName());
        if (sbTeam != null && sbTeam.hasEntry(player.getName())) {
            sbTeam.removeEntry(player.getName());
        }

        sbTeam = player.getScoreboard().getTeam(team.getName());
        if (sbTeam != null && !sbTeam.hasEntry(player.getName())) {
            sbTeam.addEntry(player.getName());
            System.out.println("Added player to this team");
        }


        gamePlayer.setTeam(team);
        players.get(team).addPlayer(gamePlayer);
    }

    public GameTeam<GamePlayer> getTeam(TeamType type) {
        if (type == TeamType.NONE) return null;

        return players.get(type);
    }

    public Pair<TeamType, GameTeam<GamePlayer>> getTeam(Player player) {
        for (Map.Entry<TeamType, GameTeam<GamePlayer>> entry : players.entrySet()) {
            if (entry.getValue().containsPlayer(player)) {
                return Pair.of(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }
}
