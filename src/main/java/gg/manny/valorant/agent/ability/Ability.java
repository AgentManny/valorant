package gg.manny.valorant.agent.ability;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.util.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.ingot.Hotbar;
import org.ipvp.ingot.HotbarAction;
import org.ipvp.ingot.HotbarApi;

import java.util.Collections;

import static gg.manny.valorant.agent.AgentListener.ABILITY_METADATA;

public interface Ability {

    AbilityType ability();

    AbilityPrice priceType();

    int slot();

    default int cooldown() {
        return priceType() == AbilityPrice.FREE ? 30 : 0;
    }

    default int price() {
        return 0;
    }

    void tick(Player player);

    boolean activate(Player player, HotbarAction action);

    default void runVisualTimer(Player player) {
        new BukkitRunnable() {

            private ItemStack original = ability().getItem().clone();

            private int timer = cooldown();
            private ItemStack visualTimer = new ItemBuilder(Material.GRAY_DYE)
                    .name(ChatColor.RED + ability().getName())
                    .lore(Collections.singletonList(ABILITY_METADATA))
                    .amount(timer)
                    .create();

            @Override
            public void run() {
                if (player == null) {
                    cancel();
                    return;
                }

                Hotbar hotbar = HotbarApi.getCurrentHotbar(player);
                if (hotbar == null) {
                    cancel();
                    return;
                }

                visualTimer.setAmount(timer);
                HotbarApi.getCurrentHotbar(player).getSlot(slot()).setItem(--timer < 0 ? original : visualTimer);
                player.updateInventory();

                if (timer < 0) {
                    cancel();
                    return;
                }
            }

        }.runTaskTimer(Valorant.getInstance(), 20L, 20L);
    }


}
