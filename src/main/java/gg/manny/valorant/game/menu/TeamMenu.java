package gg.manny.valorant.game.menu;

import gg.manny.valorant.game.Team;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select your team:";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new TeamButton(Team.ATTACKERS));
        buttons.put(14, new TeamButton(Team.DEFENDERS));
        buttons.put(26, new TeamButton(Team.SPECTATORS));

        return buttons;
    }

    @AllArgsConstructor
    private class TeamButton extends Button {

        private Team team;

        @Override
        public String getName(Player player) {
            return team.getColor() + team.getName();
        }

        @Override
        public List<String> getDescription(Player player) {
            List<String> lines = new ArrayList<>();
            lines.add(ChatColor.GRAY + "Click to join this team.");
            return lines;
        }

        @Override
        public Material getMaterial(Player player) {
            return team.getIcon();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            player.sendMessage(ChatColor.RED + "Looks like this isn't supported yet.");
        }
    }
}