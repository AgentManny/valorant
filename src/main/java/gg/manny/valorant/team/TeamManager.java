package gg.manny.valorant.team;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.game.TeamType;
import gg.manny.valorant.player.GamePlayer;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamManager {

    private final Valorant plugin;

    private Team<GamePlayer> teamOne = new Team<>();
    private Team<GamePlayer> teamTwo = new Team<>();
    private Team<GamePlayer> teamSpectator = new Team<>();

    private Map<TeamType, Team<GamePlayer>> players = new HashMap<>();

    public TeamManager(Valorant plugin) {
        this.plugin = plugin;

        boolean randomTeam = Valorant.RANDOM.nextBoolean();
        players.put(TeamType.ATTACKERS, randomTeam ? teamOne : teamTwo);
        players.put(TeamType.DEFENDERS, randomTeam ? teamTwo : teamOne);
        players.put(TeamType.SPECTATORS, teamSpectator);
    }

    public boolean containsPlayer(Player player) {
        for (Team<GamePlayer> team : players.values()) {
            if (team.containsPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public void setTeam(TeamType team, Player player) {
        GamePlayer gamePlayer = plugin.getPlayerManager().getByPlayer(player);
        gamePlayer.setTeam(team);
        players.get(team).addPlayer(gamePlayer);
    }

    public Team<GamePlayer> getTeam(TeamType type) {
        if (type == TeamType.NONE) return null;

        return players.get(type);
    }

    public Pair<TeamType, Team<GamePlayer>> getTeam(Player player) {
        for (Map.Entry<TeamType, Team<GamePlayer>> entry : players.entrySet()) {
            if (entry.getValue().containsPlayer(player)) {
                return Pair.of(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }
}
