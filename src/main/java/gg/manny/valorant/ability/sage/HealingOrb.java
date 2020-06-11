package gg.manny.valorant.ability.sage;

import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.ability.AbilityPrice;
import gg.manny.valorant.ability.AbilityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class HealingOrb implements Ability {

    @Override
    public AbilityType ability() {
        return AbilityType.HEALING_ORB;
    }

    @Override
    public int cooldown() {
        return 45;
    }

    @Override
    public AbilityPrice priceType() {
        return AbilityPrice.FREE;
    }

    @Override
    public void activate(Player player, Action action) {

    }
}
