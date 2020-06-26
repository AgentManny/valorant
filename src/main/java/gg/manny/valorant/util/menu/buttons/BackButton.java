package gg.manny.valorant.util.menu.buttons;

import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BackButton extends Button {

    private Menu back;

    @Override
    public Material getMaterial(Player player) {
        return Material.RED_BED;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public String getName(Player player) {
        return "\u00a7cGo back";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }
}

