package gg.manny.valorant.agent.menu;

import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.util.ItemBuilder;
import gg.manny.valorant.util.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class AgentInfoButton extends Button {

    private Agent agent;

    public AgentInfoButton(Agent agent, boolean b) {
        this.agent = agent;
    }

    @Override
    public String getName(Player player) {
        return agent.getDisplayName();
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = ItemBuilder.wrap(agent.getDescription(), ChatColor.GRAY.toString(), 45);
        for (Ability ability : agent.getAbilities()) {
            lines.add(" ");
            lines.add(ChatColor.AQUA + ability.getName());
            List<String> data = ItemBuilder.wrap(ability.getDescription(), ChatColor.GRAY.toString(), 45);
            lines.addAll(data);
        }
        return lines;
    }


    @Override
    public Material getMaterial(Player player) {
        return agent.getIcon();
    }
}
