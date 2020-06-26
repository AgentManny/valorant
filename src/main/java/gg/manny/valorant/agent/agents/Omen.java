package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Omen extends Agent {

    public Omen() {
        super("Omen", AgentCategory.CONTROLLER);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Material getIcon() {
        return Material.PURPLE_DYE;
    }

    @Override
    public String getDescription() {
        return "A phantom of a memory, Omen hunts in the shadows." +
                " He renders enemies blind, teleports across the field, " +
                "then lets paranoia take hold as foes scramble to uncover where it might strike next.";
    }
}
