package gg.manny.valorant.agent.agents;

import gg.manny.valorant.ability.AbilityType;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.agent.AgentType;

import java.util.Arrays;

public class Sage extends Agent {

    public Sage() {
        super("Sage", AgentType.SENTINEL);

        abilities = Arrays.asList(AbilityType.HEALING_ORB);
    }

}
