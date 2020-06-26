package gg.manny.valorant.agent.menu.button;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.agent.ability.AbilityType;
import gg.manny.valorant.util.menu.Button;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import gg.manny.valorant.util.ItemBuilder;

import java.util.List;

@AllArgsConstructor
public class AgentInfoButton extends Button {

    private Agent agent;
    private boolean selectable; // Used to allow players to select agents

    @Override
    public String getName(Player player) {
        return ChatColor.AQUA + agent.getName();
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.DIRT;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = ItemBuilder.wrap(agent.description(), ChatColor.GRAY.toString(), 45);
        for (AbilityType ability : agent.getAbilities()) {
            lines.add(" ");
            lines.add(ChatColor.AQUA + ability.getName());
            List<String> data = ItemBuilder.wrap(ability.getDescription(), ChatColor.GRAY.toString(), 45);
            lines.addAll(data);
        }
        if (selectable) {
            lines.add(" ");
            lines.add(ChatColor.YELLOW + "Lock " + agent.getName() + " agent.");
        }
        return lines;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (clickType.isLeftClick() && selectable) {
            player.sendMessage(ChatColor.YELLOW + "Selected " + ChatColor.GREEN + agent.getName() + ChatColor.YELLOW + " as playing agent.");
            Valorant.getInstance().getAgentManager().setAgent(player, agent);
        }
    }
}
