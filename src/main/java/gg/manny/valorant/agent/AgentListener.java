package gg.manny.valorant.agent;

import gg.manny.valorant.agent.ability.Ability;
import gg.manny.valorant.agent.ability.AbilityType;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class AgentListener implements Listener {

    public static final String ABILITY_METADATA = ChatColor.DARK_GRAY + "ABILITY";

    private final AgentManager agentManager;

    @EventHandler
    public void onAbilityActivate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Agent agent = agentManager.getAgent(player);
        if (agent == null) return;

        if (event.hasItem()) {
            ItemStack item = event.getItem();
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                String entry = item.getItemMeta().getLore().get(0);
                if (entry.equalsIgnoreCase(ABILITY_METADATA)) {
                    player.sendMessage(ChatColor.RED + "You can't use this for another " + item.getAmount() + " second" + (item.getAmount() < 0 ? "" : "s") + ".");
                    return;
                }
            }
            for (AbilityType type : agent.getAbilities()) {
                if (type.getIcon() == item.getType()) {
                    Ability ability = agentManager.getAbilities().get(type);
                    if (ability.activate(player, event.getAction())) {
                        ability.runVisualTimer(player);
                    }
                }
            }
        }
    }

}
