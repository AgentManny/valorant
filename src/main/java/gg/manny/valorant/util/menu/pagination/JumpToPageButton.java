package gg.manny.valorant.util.menu.pagination;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import gg.manny.valorant.util.menu.Button;

import java.util.List;

@AllArgsConstructor
public class JumpToPageButton extends Button {

    private int page;
    private PaginatedMenu menu;

    @Override
    public String getName(Player player) {
        return "\u00a7ePage " + this.page;
    }

    @Override
    public List<String> getDescription(Player player) {
        return null;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.BOOK;
    }

    @Override
    public int getAmount(Player player) {
        return this.page;
    }

    @Override
    public byte getDamageValue(Player player) {
        return 0;
    }

    @Override
    public void clicked(Player player, int i, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }

}

