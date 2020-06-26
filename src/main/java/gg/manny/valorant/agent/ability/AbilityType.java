package gg.manny.valorant.agent.ability;

import gg.manny.valorant.Valorant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import gg.manny.valorant.util.ItemBuilder;

@Getter
@AllArgsConstructor
public enum AbilityType {

    HEALING_ORB(
            "Healing Orb",
            ChatColor.RED,
            Material.EMERALD,
            "EQUIP a healing orb. Left Click with your crosshairs over a damaged ally to activate a heal-over-time on them. Right Click while Sage is damaged to activate a self heal-over-time."
    );

    private String name;
    private ChatColor color;
    private Material icon;
    private String description;

    public ItemStack getItem() {
        return new ItemBuilder(icon)
                .name(color + name)
                .lore(ItemBuilder.wrap(description, ChatColor.GRAY.toString(), 45))
                .create();
    }

    public Ability get() {
        return Valorant.getInstance().getAgentManager().getAbilities().get(this);
    }

}
