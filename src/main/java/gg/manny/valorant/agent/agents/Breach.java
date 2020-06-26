package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Breach extends Agent {
    public Breach() {
        super("Breach", AgentCategory.INITIATOR);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_GRAY;
    }

    @Override
    public Material getIcon() {
        return Material.ZOMBIE_VILLAGER_SPAWN_EGG;
    }

    @Override
    public String getDescription() {
        return "The bionic Swede Breach fires powerful, targeted kinetic blasts to aggressively clear a path through enemy ground." +
                " The damage and disruption he inflicts ensures no fight is ever fair.";
    }
}
