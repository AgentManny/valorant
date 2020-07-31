package gg.manny.valorant.ability;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ipvp.ingot.HotbarAction;

@Getter
@RequiredArgsConstructor
public abstract class Ability {

    private final String name;

    private final AbilitySkill skill;
    private final AbilityPrice price;

    protected int cooldown = 0;

    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    public String getDisplayName() {
        return getColor() + this.name;
    }

    public String getId() {
        return name.toUpperCase().replace(" ", "_");
    }

    public int getSlot() {
        return skill.getSlot(); // Some stuff like Reyna abilities override slot data
    }

    public int getCost() {
        return 0;
    }

    public abstract Material getIcon();

    public abstract String getDescription();

    public boolean hasCooldown(Player player) {
        GamePlayer gamePlayer = Valorant.getInstance().getPlayerManager().getByPlayer(player);
        return gamePlayer != null && gamePlayer.getCooldowns().containsKey(this);
    }

    public void addCooldown(Player player) {
        if (cooldown > 0) {
            GamePlayer gamePlayer = Valorant.getInstance().getPlayerManager().getByPlayer(player);
            gamePlayer.getCooldowns().put(this, cooldown);
        }
    }

    public abstract boolean activate(Player player, HotbarAction action);

    public void tick(Player player) {

    }

    public GamePlayer getPlayer(Player player) {
        return Valorant.getInstance().getPlayerManager().getByPlayer(player);
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

    @Getter
    @AllArgsConstructor
    public enum AbilitySkill {

        BASIC(3), SIGNATURE(4), ULTIMATE(5);

        private int slot;

    }
}
