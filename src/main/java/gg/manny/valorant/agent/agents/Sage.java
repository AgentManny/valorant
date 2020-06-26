package gg.manny.valorant.agent.agents;

import gg.manny.valorant.ability.sage.HealingOrb;
import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Sage extends Agent {

    public Sage() {
        super("Sage", AgentCategory.SENTINEL);

        this.abilities.add(new HealingOrb());
    }

    @Override
    public ChatColor color() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public Material icon() {
        return Material.HEART_OF_THE_SEA;
    }

    @Override
    public String description() {
        return "The bastion of China, Sage creates safety for herself and her team wherever she goes. " +
                "Able to revive fallen friends and stave off forceful assaults, " +
                "she provides a calm center to a hellish battlefield.";
    }
}
