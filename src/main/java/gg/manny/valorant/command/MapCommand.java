package gg.manny.valorant.command;

import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.map.GameMap;
import gg.manny.valorant.map.MapManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class MapCommand implements CommandExecutor {

    private final Valorant plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("valorant.command.map")) {
            sender.sendMessage(Locale.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Locale.SYSTEM_PREFIX + "Map commands:");
            sender.sendMessage(new String[] {
                    "/map create <name>",
                    "/map setdescription <name> [description...]",
                    "/map info <name>",
                    "/map remove <name>",
                    "/map list",
                    "/map location <list|add|remove> [name]"
            });
            return true;
        }

        MapManager mapManager = plugin.getMapManager();
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /map create <name>");
                return true;
            }

            String name = args[1];
            if (mapManager.getMapByName(name) != null) {
                sender.sendMessage(Locale.SYSTEM_PREFIX + "Map " + ChatColor.LIGHT_PURPLE + name + ChatColor.RESET + " already exists.");
                return true;
            }

            sender.sendMessage(Locale.SYSTEM_PREFIX + "Created " + ChatColor.LIGHT_PURPLE + name + ChatColor.RESET + " map.");
            mapManager.getMaps().add(new GameMap(name));
        } else if (args[0].equalsIgnoreCase("list")) {
            if (mapManager.getMaps().isEmpty()) {
                sender.sendMessage(ChatColor.RED + "There aren't any maps created.");
                return true;
            }

            sender.sendMessage(Locale.SYSTEM_PREFIX + "Maps (" + mapManager.getMaps().size() + "): ");
            for (GameMap map : mapManager.getMaps()) {
                sender.sendMessage(ChatColor.WHITE + " - " + map.getName() + ": " + ChatColor.GRAY + map.getDescription());
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /map remove <name>");
                return true;
            }

            GameMap map = mapManager.getMapByName(args[1]);
            if (map == null) {
                sender.sendMessage(Locale.SYSTEM_PREFIX + "Map " + args[1] + " not found.");
                return true;
            }

            sender.sendMessage(Locale.SYSTEM_PREFIX + "Removed"  + map.getName() + ".");
        }

        return true;
    }
}
