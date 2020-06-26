package gg.manny.valorant.agent.agents;

import gg.manny.valorant.ability.sage.HealingOrb;
import gg.manny.valorant.ability.sage.SlowOrb;
import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

public class Sage extends Agent {

    public Sage() {
        super("Sage", AgentCategory.SENTINEL);

        this.abilities.addAll(Arrays.asList(
                new HealingOrb(),
                new SlowOrb()
        ));
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_AQUA;
    }

    @Override
    public Material getIcon() {
        return Material.DOLPHIN_SPAWN_EGG;
    }

    @Override
    public String getDescription() {
        return "The bastion of China, Sage creates safety for herself and her team wherever she goes. " +
                "Able to revive fallen friends and stave off forceful assaults, " +
                "she provides a calm center to a hellish battlefield.";
    }
}
