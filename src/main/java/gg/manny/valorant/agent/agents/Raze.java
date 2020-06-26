package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Raze extends Agent {
    public Raze() {
        super("Raze", AgentCategory.DUALIST);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public Material getIcon() {
        return Material.FIRE_CHARGE;
    }

    @Override
    public String getDescription() {
        return "Raze explodes out of Brazil with her big personality and big guns." +
                " With her blunt-force-trauma playstyle, she excels at flushing entrenched enemies and clearing tight spaces with a generous dose of boom.";
    }
}
