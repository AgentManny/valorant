package gg.manny.valorant.agent.agents;

import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Viper extends Agent {
    public Viper() {
        super("Viper", AgentCategory.CONTROLLER);
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public Material getIcon() {
        return Material.CREEPER_SPAWN_EGG;
    }

    @Override
    public String getDescription() {
        return "The American Chemist, Viper deploys an array of poisonous chemical devices to control the battlefield and cripple the enemy’s vision." +
                " If the toxins don’t kill her prey, her mind games surely will.";
    }
}
