package gg.manny.valorant.util.menu.menus;

import gg.manny.valorant.util.Callback;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import gg.manny.valorant.util.menu.buttons.BooleanButton;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ConfirmMenu extends Menu {

    private String title;
    private Callback<Boolean> response;

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for (int i = 0; i < 9; ++i) {
            if (i == 3) {
                buttons.put(i, new BooleanButton(true, this.response));
                continue;
            }
            if (i == 5) {
                buttons.put(i, new BooleanButton(false, this.response));
                continue;
            }
            buttons.put(i, Button.placeholder(Material.RED_STAINED_GLASS_PANE));
        }
        return buttons;
    }

    @Override
    public String getTitle(Player player) {
        return this.title;
    }

}

