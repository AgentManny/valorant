package gg.manny.valorant.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public interface Ability {

    AbilityType ability();

    AbilityPrice priceType();

    default int cooldown() {
        return priceType() == AbilityPrice.FREE ? 30 : 0;
    }

    default int price() {
        return 0;
    }

    void activate(Player player, Action action);

}
