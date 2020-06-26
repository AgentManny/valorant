package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Reyna extends Agent {
    public Reyna() {
        super("Reyna", AgentCategory.DUALIST);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public Material getIcon() {
        return Material.END_CRYSTAL;
    }

    @Override
    public String getDescription() {
        return "Forged in the heart of Mexico, Reyna dominates single combat, popping off with each kill she scores." +
                " Her capability is only limited by her raw skill, making her sharply dependent on performance.";
    }
}
