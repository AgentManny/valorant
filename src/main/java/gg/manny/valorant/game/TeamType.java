package gg.manny.valorant.game;

import gg.manny.valorant.Valorant;
import gg.manny.valorant.player.GamePlayer;
import gg.manny.valorant.team.GameTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum TeamType {

    ATTACKERS("Attackers", Material.RED_BANNER, "T", ChatColor.RED),
    DEFENDERS("Defenders", Material.CYAN_BANNER,"CT", ChatColor.BLUE),
    SPECTATORS("Spectators", Material.GRAY_BANNER, "SPEC", ChatColor.GRAY),
    NONE("None", Material.RED_BED, "???", ChatColor.GRAY);

    private String name;
    private Material icon;

    private String prefix;
    private ChatColor color;

    public String getDisplayName() {
        return color + name;
    }

    public String getBroadcastPrefix() {
        return color + "(" + name + ") " + ChatColor.WHITE;
    }

    public GameTeam getTeam() {
        return Valorant.getInstance().getTeamManager().getTeam(this);
    }

}
