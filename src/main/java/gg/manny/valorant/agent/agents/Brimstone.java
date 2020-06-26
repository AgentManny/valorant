package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Brimstone extends Agent {
    public Brimstone() {
        super("Brimstone", AgentCategory.CONTROLLER);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLACK;
    }

    @Override
    public Material getIcon() {
        return Material.ENDERMITE_SPAWN_EGG;
    }

    @Override
    public String getDescription() {
        return "Joining from the USA, Brimstone’s orbital arsenal ensures his squad always has the advantage." +
                " His ability to deliver utility precisely and safely make him the unmatched boots-on-the-ground commander.";
    }
}
