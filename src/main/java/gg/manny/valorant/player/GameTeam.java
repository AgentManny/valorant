package gg.manny.valorant.player;

import gg.manny.valorant.game.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GameTeam<T extends GamePlayer> {

    private final Team team;
    private List<T> gamePlayers = new ArrayList<>();

    public boolean containsPlayer(Player player) {
        for (T gamePlayer : this.gamePlayers) {
            if (gamePlayer.getUuid().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void addPlayer(T player) {
        this.gamePlayers.add(player);
    }

    public void removePlayer(T player) {
        this.gamePlayers.remove(player);
    }


    public GamePlayer getGamePlayer(Player player) {
        for (T gamePlayer : this.gamePlayers) {
            if (gamePlayer.getUuid().equals(player.getUniqueId())) {
                return gamePlayer;
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        this.gamePlayers.forEach(matchPlayer -> {
            Player player = matchPlayer.getPlayer();

            if (player != null) {
                players.add(player);
            }
        });

        return players;
    }

    /**
     * Returns a list of objects that extend {@link GamePlayer} whose
     * {@link GamePlayer#isAlive()} returns true.
     *
     * @return A list of team players that are alive.
     */
    public List<T> getAlivePlayers() {
        List<T> alive = new ArrayList<>();

        this.gamePlayers.forEach(player -> {
            if (player.isAlive()) {
                alive.add(player);
            }
        });

        return alive;
    }

    /**
     * Returns an integer that is incremented for each {@link GamePlayer}
     * element in the {@code gamePlayers} list whose {@link GamePlayer#isAlive()}
     * returns true.
     *
     * Use this method rather than calling {@link List#size()} on
     * the result of {@code getAliveGamePlayers}.
     *
     * @return The count of team players that are alive.
     */
    public int getAliveCount() {

        int alive = 0;

        for (T player : this.gamePlayers) {
            if (player.isAlive()) {
                alive++;
            }
        }

        return alive;

    }

    public int getSize() {
        return this.gamePlayers.size();
    }

    /**
     * Returns a list of objects that extend {@link GamePlayer} whose
     * {@link GamePlayer#isAlive()} returns false.
     *
     * @return A list of team players that are dead.
     */
    public List<T> getDeadGamePlayers() {
        List<T> dead = new ArrayList<>();
        this.gamePlayers.forEach(player -> {
            if (!player.isAlive()) {
                dead.add(player);
            }
        });

        return dead;
    }
    
    public int getDeadCount() {
        return this.gamePlayers.size() - this.getAliveCount();
    }

    public void broadcast(String messages) {
        this.getPlayers().forEach(player -> player.sendMessage(messages));
    }

    public void broadcast(List<String> messages) {
        this.getPlayers().forEach(player -> messages.forEach(player::sendMessage));
    }

    public void broadcast(BaseComponent... component) {
        this.getPlayers().forEach(player -> player.spigot().sendMessage(component));
    }


}
