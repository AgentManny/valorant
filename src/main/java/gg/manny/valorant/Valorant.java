package gg.manny.valorant;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Valorant extends JavaPlugin {

    @Getter
    private static Valorant instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }
}
