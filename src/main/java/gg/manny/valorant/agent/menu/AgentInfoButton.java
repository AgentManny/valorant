package gg.manny.valorant.agent.menu;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.util.ItemBuilder;
import gg.manny.valorant.util.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

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

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        Valorant.getInstance().getAgentManager().setAgent(player, agent);
        player.sendMessage(ChatColor.GRAY + "Your agent is now " + agent.getColor().toString() + agent.getName());
        player.closeInventory();
        player.sendTitle(agent.getColor().toString() + agent.getName(), ChatColor.GRAY + "Selected. " , 10, 30, 10);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);

    }
}
