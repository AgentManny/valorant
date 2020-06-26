package gg.manny.valorant.util.menu.buttons;

import gg.manny.valorant.util.Callback;
import gg.manny.valorant.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BooleanButton extends Button {

    private boolean confirm;
    private Callback<Boolean> callback;

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 20.0f, 0.1f);
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_GRAVEL_BREAK, 20.0f, 0.1f);
        }
        player.closeInventory();
        this.callback.callback(this.confirm);
    }

    @Override
    public String getName(Player player) {
        return this.confirm ? "\u00a7aConfirm" : "\u00a7cCancel";
    }

    @Override
    public List<String> getDescription(Player player) {
        return new ArrayList<>();
    }

    @Override
    public Material getMaterial(Player player) {
        return this.confirm ? Material.GREEN_WOOL : Material.RED_WOOL;
    }

}

