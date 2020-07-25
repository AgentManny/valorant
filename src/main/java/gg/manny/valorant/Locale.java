package gg.manny.valorant;

import gg.manny.valorant.game.menu.GameVoteMenu;
import gg.manny.valorant.team.menu.TeamSelectMenu;
import gg.manny.valorant.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.Slot;
import org.ipvp.ingot.type.VanillaHotbar;

public class Locale {

    public static String BROADCAST_PREFIX = ChatColor.YELLOW + "(Broadcast) " + ChatColor.RESET;
    public static String SYSTEM_PREFIX = ChatColor.LIGHT_PURPLE + "(System) " + ChatColor.RESET;

    public static String NO_PERMISSION = SYSTEM_PREFIX + "You don't have permission to use this.";

    public static Hotbar GAME_LOBBY_HOTBAR = new VanillaHotbar();

    static {
        Slot slot = GAME_LOBBY_HOTBAR.getSlot(0);
        slot.setItem(new ItemBuilder(Material.CLOCK)
                .name(ChatColor.RED + "Select your team")
                .create()
        );
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new TeamSelectMenu().openMenu(player);
            }
        });

        slot = GAME_LOBBY_HOTBAR.getSlot(1);
        slot.setItem(new ItemBuilder(Material.BOOK)
                .name(ChatColor.LIGHT_PURPLE + "Map voting")
                .create()
        );
        slot.setActionHandler((player, action) -> {
            if (action.getType().isRightClick()) {
                new GameVoteMenu(Valorant.getInstance().getLobby()).openMenu(player);
            }
        });
    }

}
