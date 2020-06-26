package gg.manny.valorant.lobby;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.agent.menu.AgentSelector;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.Slot;
import org.ipvp.ingot.type.VanillaHotbar;

import javax.swing.text.html.parser.Entity;

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
                player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN,10,1);
            } else if (action.getType().isLeftClick()) { //DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG
                int effects = EntityEffect.values().length;
                //Sound effect = Sound.values()[Valorant.RANDOM.nextInt(effects - 1)];
                Sound effect = Sound.BLOCK_SNOW_STEP;
                player.playSound(player.getLocation(), effect, 10, 1);
                player.sendMessage(ChatColor.GRAY + "debug - " + ChatColor.YELLOW + effect.toString());
            }
        });
    }
}
