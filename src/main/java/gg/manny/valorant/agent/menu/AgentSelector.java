package gg.manny.valorant.agent.menu;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AgentSelector extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Select your agent!";
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        int x = 1, y = 1;
        for (Agent agent : Valorant.getInstance().getAgentManager().getAgents()) {
            buttonMap.put(getSlot(x, y), new AgentInfoButton(agent, true));

            if (x++ >= 7) {
                x = 1;
                y++;
            }
        }

        return buttonMap;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return super.size(buttons) + 9;
    }
}
