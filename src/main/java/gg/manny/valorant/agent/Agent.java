package gg.manny.valorant.agent;

import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.util.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.HotbarApi;
import org.ipvp.ingot.Slot;
import org.ipvp.ingot.type.VanillaHotbar;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class Agent {

    private final String name;
    private final AgentCategory type;

    protected List<Ability> abilities = new ArrayList<>();

    public String getDisplayName() {
        return this.getColor() + this.name;
    }

    public abstract ChatColor getColor();

    public abstract Material getIcon();

    public abstract String getDescription();

    public void activate(Player player) {
        Hotbar hotbar = new VanillaHotbar();

        hotbar.getSlot(0).setItem(new ItemStack(Material.DIAMOND_SWORD));
        hotbar.getSlot(8).setItem(new ItemStack(Material.NETHER_STAR));


        for (Ability ability : abilities) {
            Slot slot = hotbar.getSlot(ability.getSlot());
            slot.setActionHandler((clicker, action) -> {
                if (!ability.hasCooldown(player)) {
                    if (ability.activate(player, action)) {
                        ability.addCooldown(player);
                    }
                }
            });
            slot.setItem(ability.getItem());
        }

        HotbarApi.setCurrentHotbar(player, hotbar);
    }

    public void deactivate(Player player) {
        HotbarApi.setCurrentHotbar(player, null);
    }

    public ItemStack getItem() {
        return new ItemBuilder(getIcon())
                .name(getDisplayName())
                .lore(ItemBuilder.wrap(getDescription(), ChatColor.GRAY.toString(), 45))
                .create();
    }

    public enum AgentCategory {

        CONTROLLER,
        DUALIST,
        INITIATOR,
        SENTINEL;

    }

}
