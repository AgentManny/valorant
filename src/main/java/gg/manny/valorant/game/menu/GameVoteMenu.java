package gg.manny.valorant.game.menu;

import gg.manny.valorant.Locale;
import gg.manny.valorant.game.GameLobby;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class GameVoteMenu extends Menu {
    {
        setUpdateAfterClick(true);
        setAutoUpdate(true);
    }

    private final GameLobby lobby;

    @Override
    public String getTitle(Player player) {
        return "Map voting";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        AtomicInteger size = new AtomicInteger();
        lobby.votes.forEach((name, votes) -> buttons.put(size.incrementAndGet(), new VoteButton(name, votes.size())));
        return buttons;
    }

    @RequiredArgsConstructor
    private class VoteButton extends Button {

        private final String map;
        private final int votes;

        @Override
        public String getName(Player player) {
            return ChatColor.LIGHT_PURPLE + map;
        }

        @Override
        public int getAmount(Player player) {
            return Math.max(votes, 1);
        }

        @Override
        public List<String> getDescription(Player player) {
            return Collections.singletonList(ChatColor.GRAY + "Votes: " + ChatColor.WHITE + votes);
        }

        @Override
        public Material getMaterial(Player player) {
            return lobby.hasVoted(player, map) ? Material.ENCHANTED_BOOK : Material.BOOK;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            boolean value = lobby.hasVoted(player, map);
            if (value) {
                player.sendMessage(Locale.SYSTEM_PREFIX + "You've already voted for this map.");
            } else {
                player.sendMessage(Locale.SYSTEM_PREFIX + "You've voted for " + map + " map.");
                lobby.setVote(player, map);
            }
        }
    }


}
