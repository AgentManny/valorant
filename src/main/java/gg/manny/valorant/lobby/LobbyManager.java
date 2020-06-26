package gg.manny.valorant.lobby;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.Agent;
import gg.manny.valorant.agent.menu.AgentSelector;
import gg.manny.valorant.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        Slot slot = SELECT_HOTBAR.getSlot(4);
        slot.setItem(new ItemStack(Material.COMPASS));
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new AgentSelector().openMenu(player);
            }
        });
    }
}
