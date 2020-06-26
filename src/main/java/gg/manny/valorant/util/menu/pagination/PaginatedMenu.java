package gg.manny.valorant.util.menu.pagination;

import lombok.Getter;
import org.bukkit.entity.Player;
import gg.manny.valorant.util.menu.Button;
import gg.manny.valorant.util.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedMenu extends Menu {

    @Getter private int page = 1;

    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player) + " - " + this.page + "/" + this.getPages(player);
    }

    public final void modPage(Player player, int mod) {
        this.page += mod;
        this.getButtons().clear();
        this.openMenu(player);
    }

    public final int getPages(Player player) {
        int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int) Math.ceil((double) buttonAmount / (double) this.getMaxItemsPerPage(player));
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int) ((double) (this.page - 1) * (double) this.getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) this.page * (double) this.getMaxItemsPerPage(player));
        HashMap<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new PageButton(-1, this));
        buttons.put(8, new PageButton(1, this));
        for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind < minIndex || ind >= maxIndex) continue;
            buttons.put(ind - ((int) ((double) this.getMaxItemsPerPage(player) * (double) (this.page - 1)) - 9), entry.getValue());
        }
        Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            for (Map.Entry gent : global.entrySet()) {
                buttons.put((Integer) gent.getKey(), (Button) gent.getValue());
            }
        }
        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 18;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(Player var1);

    public abstract Map<Integer, Button> getAllPagesButtons(Player var1);

}

