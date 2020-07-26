package gg.manny.valorant.listener;

import joptsimple.internal.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    public static double SPEED_REDUCER = 0.35;


    public static void debug(String key, String... messages) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[Debug] [" + key + "]: (" + Strings.join(messages, ") (") + ")");
    }
}
