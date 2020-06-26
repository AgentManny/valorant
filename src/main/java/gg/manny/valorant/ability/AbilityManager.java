package gg.manny.valorant.ability;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.tasks.AbilityCooldownTask;
import gg.manny.valorant.ability.tasks.AbilityVisualTask;
import lombok.Getter;

import java.util.UUID;

public class AbilityManager {

    @Getter private Table<UUID, Ability, Integer> playerCooldowns = HashBasedTable.create(); // Integer instead of timestamp as we update stuff.

    public AbilityManager(Valorant plugin) {

        // Tasks
        new AbilityVisualTask(plugin).runTaskTimer(plugin, 2L, 2L);
        new AbilityCooldownTask(this, plugin).runTaskTimer(plugin, 20L, 20L);
    }

}
