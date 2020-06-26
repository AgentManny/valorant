package gg.manny.valorant.lobby;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.menu.AgentSelector;
import gg.manny.valorant.game.menu.TeamMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
        Slot slot = SELECT_HOTBAR.getSlot(1);
        slot.setItem(new ItemStack(Material.BOOK));
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new AgentSelector().openMenu(player);
            }
        });

        slot = SELECT_HOTBAR.getSlot(0);
        slot.setItem(new ItemStack(Material.CLOCK));
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new TeamMenu().openMenu(player);
            }
        });
    }
}
