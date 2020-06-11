package gg.manny.valorant.agent;

import gg.manny.valorant.ability.AbilityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class Agent {

    private final String name;
    private final AgentType type;

    protected List<AbilityType> abilities = new ArrayList<>();


}
