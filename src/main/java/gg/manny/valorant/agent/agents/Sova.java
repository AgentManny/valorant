package gg.manny.valorant.agent.agents;

import gg.manny.valorant.ability.sova.darts.ReconDart;
import gg.manny.valorant.agent.Agent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Sova extends Agent {
    public Sova() {
        super("Sova", AgentCategory.INITIATOR);

        this.abilities.add(new ReconDart());
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Material getIcon() {
        return Material.CROSSBOW;
    }

    @Override
    public String getDescription() {
        return "Born from the eternal winter of Russia's tundra, Sova tracks, finds, and eliminates enemies with ruthless efficiency and precision." +
                " His custom bow and incredible scouting abilities ensure that even if you run, you cannot hide.";
    }

}
