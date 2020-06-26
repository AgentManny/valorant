package gg.manny.valorant.agent;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.agents.Sage;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class AgentManager {

    private Set<Agent> agents = new HashSet<>();
    private Map<UUID, Agent> playerAgents = new HashMap<>();

    public AgentManager(Valorant plugin) {
        agents.addAll(Arrays.asList(
                new Sage()
        ));


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
