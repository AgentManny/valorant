package gg.manny.valorant.ability;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.ingot.HotbarAction;

@Getter
@AllArgsConstructor
public abstract class Ability {

    private final String name;
    private final AbilityType type;

    private ChatColor color;

    public String getDisplayName() {
        return this.color + this.name;
    }

    public AbilityPrice getPrice() {
        return AbilityPrice.FREE;
    }

    public int getCost() {
        return 0;
    }

    public abstract Material getIcon();

    public abstract String getDescription();

    public int getCooldown() {
        return 0;
    }

    public abstract int getSlot();

    public boolean hasCooldown(Player player) {
        return (Valorant.getInstance().getAbilityManager().getPlayerCooldowns().contains(player.getUniqueId(), this));
    }

    public void addCooldown(Player player) {
        if (getCooldown() > 0) {
            Valorant.getInstance().getAbilityManager().getPlayerCooldowns().put(player.getUniqueId(), this, getCooldown());
        }
    }

    public abstract boolean activate(Player player, HotbarAction action);

    public void tick(Player player) {

    }

    public ItemStack getItem() {
        return new ItemBuilder(getIcon())
                .name(getDisplayName())
                .lore(ItemBuilder.wrap(getDescription(), ChatColor.GRAY.toString(), 45))
                .create();
    }


    public enum AbilityPrice {

        FREE, ULTIMATE_POINTS, CREDITS;

    }

    public enum AbilityType {

        BASIC, SIGNATURE, ULTIMATE

    }
}
