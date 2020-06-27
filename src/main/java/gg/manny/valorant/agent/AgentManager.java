package gg.manny.valorant.agent;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.tasks.AbilityCooldownTask;
import gg.manny.valorant.ability.tasks.AbilityVisualTask;
import gg.manny.valorant.agent.agents.*;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class AgentManager {

    private final Valorant plugin;

    private Set<Agent> agents = new LinkedHashSet<>();
    private Map<UUID, Agent> playerAgents = new HashMap<>();

    public AgentManager(Valorant plugin) {
        this.plugin = plugin;
        agents.addAll(Arrays.asList(
                new Jett(),
                new Raze(),
                new Breach(),
                new Omen(),
                new Brimstone(),
                new Phoenix(),
                new Sage(),
                new Sova(),
                new Viper(),
                new Cypher(),
                new Reyna()
        ));

        new AbilityVisualTask().runTaskTimer(plugin, 2L, 2L);
        new AbilityCooldownTask().runTaskTimer(plugin, 20L, 20L);
    }


    public Agent getAgent(String name) {
        for (Agent agent : agents) {
            if (agent.getName().equalsIgnoreCase(name)) {
                return agent;
            }
        }
        return null;
    }

    public Agent getAgent(Player player) {
        if (playerAgents.containsKey(player.getUniqueId())) {
            return playerAgents.get(player.getUniqueId());
        }
        return null;
    }

    public void setAgent(Player player, Agent agent) {
        Agent selectedAgent = getAgent(player);
        if (selectedAgent != null) {
            selectedAgent.deactivate(player);
        }

        playerAgents.put(player.getUniqueId(), agent);
        agent.activate(player);
    }
}
