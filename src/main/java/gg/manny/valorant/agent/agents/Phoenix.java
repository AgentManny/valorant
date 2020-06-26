package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Phoenix extends Agent {
    public Phoenix() {
        super("Phoenix", AgentCategory.DUALIST);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Material getIcon() {
        return Material.RED_DYE;
    }

    @Override
    public String getDescription() {
        return "Hailing from the U.K., Phoenix’s star power shines through in his fighting style, igniting the battlefield with flash and flare." +
                " Whether he’s got backup or not, he’s rushing in to fight on his own terms.";
    }
}
