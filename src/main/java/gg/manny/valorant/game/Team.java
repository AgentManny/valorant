package gg.manny.valorant.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum Team {

    ATTACKERS("Attackers", Material.RED_BANNER, "T", ChatColor.RED),
    DEFENDERS("Defenders", Material.CYAN_BANNER,"CT", ChatColor.BLUE),
    SPECTATORS("Spectators", Material.GRAY_BANNER, "SPEC", ChatColor.GRAY);

    private String name;
    private Material icon;

    private String prefix;
    private ChatColor color;

}
