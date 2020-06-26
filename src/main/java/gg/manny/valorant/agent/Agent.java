package gg.manny.valorant.agent;

import gg.manny.valorant.agent.ability.Ability;
import gg.manny.valorant.agent.ability.AbilityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
    protected List<AbilityType> abilities = new ArrayList<>();

    public abstract ChatColor color();

    public abstract Material icon();

    public abstract String description();

    public void activate(Player player) {
        Hotbar hotbar = new VanillaHotbar();
        hotbar.getSlot(0).setItem(new ItemStack(Material.DIAMOND_SWORD, 1));


        for (AbilityType type : abilities) {
            Ability ability = type.get();

            Slot slot = hotbar.getSlot(ability.slot());
            slot.setActionHandler((clicker, action) -> ability.activate(player, action));
            slot.setItem(type.getItem());
        }

        HotbarApi.setCurrentHotbar(player, hotbar);
    }

    public void deactivate(Player player) {
        HotbarApi.setCurrentHotbar(player, null);
    }


}
