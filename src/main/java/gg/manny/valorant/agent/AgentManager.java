package gg.manny.valorant.agent;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.ability.Ability;
import gg.manny.valorant.agent.ability.AbilityType;
import gg.manny.valorant.agent.ability.sage.HealingOrb;
import gg.manny.valorant.agent.agents.Sage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class AgentManager {

    private Set<Agent> agents = new HashSet<>();
    private Map<AbilityType, Ability> abilities = new HashMap<>();

    private Map<UUID, Agent> playerAgents = new HashMap<>();

    public AgentManager() {
        agents.addAll(Arrays.asList(
                new Sage()
        ));

        Arrays.asList(
                new HealingOrb()
        ).forEach(ability -> abilities.put(ability.ability(), ability));

        Valorant.getInstance().getServer().getScheduler().runTaskTimer(Valorant.getInstance(), () -> {
            playerAgents.forEach((uuid, agent) -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    for (AbilityType type : agent.getAbilities()) {
                        Ability ability = abilities.get(type);
                        ability.tick(player);
                    }
                }
            });
        }, 2L, 2L);
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
