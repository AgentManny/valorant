package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Cypher extends Agent {
    public Cypher() {
        super("Cypher", AgentCategory.SENTINEL);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.YELLOW;
    }

    @Override
    public Material getIcon() {
        return Material.END_CRYSTAL;
    }

    @Override
    public String getDescription() {
        return "The Moroccan information broker, Cypher is a one-man surveillance network who keeps tabs on the enemy's move." +
                " No secret is safe. No maneuver goes unseen. Cypher is always watching.";
    }
}
