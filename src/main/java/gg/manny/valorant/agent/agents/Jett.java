package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Jett extends Agent {
    public Jett() {
        super("Jett", AgentCategory.DUALIST);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public Material getIcon() {
        return Material.TRIDENT;
    }

    @Override
    public String getDescription() {
        return "Representing her home country of South Korea, Jett’s agile and evasive fighting style lets her take risks no one else can." +
                " She runs circles around every skirmish, cutting enemies up before they even know what hit them.";
    }
}
