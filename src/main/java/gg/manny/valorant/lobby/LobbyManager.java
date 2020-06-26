package gg.manny.valorant.lobby;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.Slot;
import org.ipvp.ingot.type.VanillaHotbar;

public class LobbyManager {

    public static Hotbar SELECT_HOTBAR = new VanillaHotbar();

    private final Valorant plugin;

    public LobbyManager(Valorant plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(new LobbyListener(), plugin);

        loadHotbars();
    }

    private void loadHotbars() {
        int i = 0;
        for (Agent agent : plugin.getAgentManager().getAgents()) {
            Slot slot = SELECT_HOTBAR.getSlot(i);
            ItemStack icon = new ItemBuilder(agent.getIcon())
                    .name(agent.getColor() + agent.getName())
                    .create();
            slot.setItem(icon);
            slot.setActionHandler((player, action) -> {
                if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK) {
                    plugin.getAgentManager().setAgent(player, agent);
                    player.sendMessage(ChatColor.YELLOW + "Selected " + ChatColor.GREEN + agent.getName() + ChatColor.YELLOW + " as playing agent.");
                }
            });
        }

    }


}
