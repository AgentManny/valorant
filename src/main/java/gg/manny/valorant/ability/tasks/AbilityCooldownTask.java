package gg.manny.valorant.ability.tasks;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.AbilityManager;
import gg.manny.valorant.util.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.ipvp.ingot.HotbarApi;
import org.ipvp.ingot.Slot;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class AbilityCooldownTask extends BukkitRunnable {

    private final AbilityManager abilityManager;
    private final Valorant plugin;

    @Override
    public void run() {
        for (UUID playerId : plugin.getAgentManager().getPlayerAgents().keySet()) {
            Player player = Bukkit.getPlayer(playerId);
            if (player == null) continue;

            Map<Ability, Integer> cooldowns = abilityManager.getPlayerCooldowns().row(player.getUniqueId());
            for (Map.Entry<Ability, Integer> entry : cooldowns.entrySet()) {
                Ability ability = entry.getKey();
                int abilitySlot = ability.getSlot();
                Slot slot = HotbarApi.getCurrentHotbar(player).getSlot(abilitySlot);

                int timer = entry.getValue();
                if (--timer < 1) {
                    slot.setItem(ability.getItem());
                    player.getInventory().setItem(abilitySlot, slot.getItem()); // Updates it, since Slot#setItem doesn't

                    player.sendMessage(ChatColor.GREEN + "You can now use " + ability.getColor() + ability.getName() + ChatColor.GREEN + " again.");
                    abilityManager.getPlayerCooldowns().remove(playerId, ability);
                } else {
                    slot.setItem(new ItemBuilder(Material.GRAY_DYE)
                            .name(ChatColor.RED + ability.getName())
                            .amount(timer)
                            .create()
                    );
                    player.getInventory().setItem(ability.getSlot(), slot.getItem());
                    player.updateInventory();

                    player.sendMessage("Debug: You have " + timer + " left on your cooldown.");
                    abilityManager.getPlayerCooldowns().put(playerId, ability, timer);
                }
            }
        }
    }

}
