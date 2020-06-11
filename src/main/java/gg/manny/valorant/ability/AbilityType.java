package gg.manny.valorant.ability;

import lombok.AllArgsConstructor;
import org.bukkit.Material;

@AllArgsConstructor
public enum AbilityType {

    HEALING_ORB(
            "Sage",
            Material.EMERALD,
            "EQUIP a healing orb. Left Click with your crosshairs over a damaged ally to activate a heal-over-time on them. Right Click while Sage is damaged to activate a self heal-over-time."
    );

    private String name;
    private Material icon;
    private String description;

}
