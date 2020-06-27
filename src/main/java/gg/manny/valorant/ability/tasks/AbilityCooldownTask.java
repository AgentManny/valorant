package gg.manny.valorant.ability.tasks;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.ingot.HotbarApi;
import org.ipvp.ingot.Slot;

import java.util.Map;

public class AbilityCooldownTask extends BukkitRunnable {

    @Override
    public void run() {
        for (GamePlayer gamePlayer : Valorant.getInstance().getPlayerManager().getPlayerMap().values()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) continue; // If they aren't online or something

            Map<Ability, Integer> cooldowns = gamePlayer.getCooldowns();
            for (Map.Entry<Ability, Integer> entry : cooldowns.entrySet()) {
                Ability ability = entry.getKey();
                int abilitySlot = ability.getSlot();
                Slot slot = HotbarApi.getCurrentHotbar(player).getSlot(abilitySlot);

                int timer = entry.getValue();
                if (timer < 1) {
                    slot.setItem(ability.getItem());
                    player.getInventory().setItem(abilitySlot, slot.getItem()); // Updates it, since Slot#setItem doesn't

                    player.sendMessage(ChatColor.GREEN + "You can now use " + ability.getColor() + ability.getName() + ChatColor.GREEN + " again.");
                    gamePlayer.getCooldowns().remove(ability);
                } else {
                    slot.setItem(new ItemBuilder(Material.GRAY_DYE)
                            .name(ChatColor.RED + ability.getName())
                            .amount(timer)
                            .create()
                    );
                    player.getInventory().setItem(ability.getSlot(), slot.getItem());
                    player.updateInventory();

                    gamePlayer.getCooldowns().put(ability, --timer);
                }
            }
        }
    }

}
