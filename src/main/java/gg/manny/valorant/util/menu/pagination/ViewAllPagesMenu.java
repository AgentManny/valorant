package gg.manny.valorant.util.menu.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;
import gg.manny.valorant.util.menu.buttons.BackButton;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ViewAllPagesMenu extends Menu {

    @NonNull @Getter PaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Jump to page";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(this.menu));
        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); ++i) {
            buttons.put(index++, new JumpToPageButton(i, this.menu));
            if ((index - 8) % 9 != 0) continue;
            index += 2;
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}

