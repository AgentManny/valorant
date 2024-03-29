package gg.manny.valorant.command;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import gg.manny.valorant.Locale;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.map.GameMap;
import gg.manny.valorant.map.MapManager;
import gg.manny.valorant.orb.Orb;
import gg.manny.valorant.orb.OrbType;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

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
                    "/map author <map> <name>",
                    "/map create <name>",
                    "/map setdescription <name> [description...]",
                    "/map info <name>",
                    "/map remove <name>",
                    "/map list",
                    "/map callouts <map> <list|add|remove>"
            });
            return true;
        }

        Player player = (Player) sender;
        MapManager mapManager = plugin.getMapManager();
        if (args[0].equalsIgnoreCase("orb")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /map orb <map> <list|add|remove>");
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                new Orb(OrbType.ULTIMATE_ABILITY, player.getLocation()).setActive(true);
            }
        } else if (args[0].equalsIgnoreCase("create")) {
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
            mapManager.save();
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

            sender.sendMessage(Locale.SYSTEM_PREFIX + "Removed "  + map.getName() + " map.");
            mapManager.remove(map);
        } else if (args[0].equalsIgnoreCase("author")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /map author <map> <name>");
                return true;
            }

            GameMap map = mapManager.getMapByName(args[1]);
            if (map == null) {
                sender.sendMessage(Locale.SYSTEM_PREFIX + "Map " + args[1] + " not found.");
                return true;
            }

            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 2; i < args.length; i++) {
                joiner.add(args[i]);
            }

            map.setAuthor(joiner.toString());
            map.save();
            sender.sendMessage(Locale.SYSTEM_PREFIX + "Author set to " + ChatColor.LIGHT_PURPLE + map.getAuthor() + ChatColor.RESET + " for " + map.getName() + " map.");
        } else if (args[0].equalsIgnoreCase("callouts")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /map callout <add|remove> <map> <callout...>");
                return true;
            }

            GameMap map = mapManager.getMapByName(args[2]);
            if (map == null) {
                sender.sendMessage(Locale.SYSTEM_PREFIX + "Map " + args[2] + " not found.");
                return true;
            }

            boolean adding;
            if (args[1].equalsIgnoreCase("list")) {
                if (map.getCallouts().isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "There aren't any locations set for " + map.getName() + ".");
                    return true;
                }

                sender.sendMessage(ChatColor.GREEN + "Registered locations for " + map.getName() + ":");
                map.getCallouts().forEach((name, location) -> {
                    sender.sendMessage(ChatColor.WHITE + name + ChatColor.GRAY + " (Height: " + location.getHeight() + ") (Width: " + location.getWidth() + ") (Area: " + location.getArea() + ")" + " (Y Min: " + location.getMinimumY() +") (Y Max:" + location.getMaximumY() + ")");
                    location.getPoints().forEach(point -> sender.sendMessage(ChatColor.GRAY + " - (" + point.getX() + ", " + point.getZ() + ")"));
                });
            } else if ((adding = args[1].equalsIgnoreCase("add")) || args[1].equalsIgnoreCase("remove")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Usage: /map callout <add|remove> <map> <callout>");
                    return true;
                }

                StringJoiner joiner = new StringJoiner(" ");
                for (int i = 3; i < args.length; i++) {
                    joiner.add(args[i]);
                }
                String callout = joiner.toString();

                if (adding) {
                    if (map.getCallouts().containsKey(callout)) {
                        sender.sendMessage(ChatColor.RED + "Callout " + callout + " already exists!");
                        return true;
                    }

                    LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(sender));
                    try {
                        Region selection = session.getSelection(BukkitAdapter.adapt(player.getWorld()));
                        if (selection instanceof Polygonal2DRegion) {
                            Polygonal2DRegion polySelection = (Polygonal2DRegion) selection;
                            sender.sendMessage(Locale.SYSTEM_PREFIX + "Added callout " + ChatColor.LIGHT_PURPLE + callout + ChatColor.RESET + " for " + ChatColor.LIGHT_PURPLE + map.getName() + ChatColor.RESET + " map.");
                            polySelection.getPoints().forEach(point -> sender.sendMessage(ChatColor.GRAY + " - (" + point.getX() + ", " + point.getZ() + ")"));
                            map.getCallouts().put(callout, polySelection);
                            map.save();
                        } else {
                            sender.sendMessage(ChatColor.RED + "Regions must be polygons:");
                            sender.sendMessage(ChatColor.GRAY + "//sel poly - " + ChatColor.WHITE + "Left-click to select first point. All subsequent points are selected by right-clicking. Every right-click will add an additional point. The top and bottom will always encompass your highest and lowest selected points.");
                            return true;
                        }
                    } catch (IncompleteRegionException e) {
                        sender.sendMessage(ChatColor.RED + "You haven't selected a region!");
                        return true;
                    }
                } else {
                    if (!map.getCallouts().containsKey(callout)) {
                        sender.sendMessage(ChatColor.RED + "Callout " + callout + " doesn't exist!");
                        return true;
                    }

                    sender.sendMessage(Locale.SYSTEM_PREFIX + "Removed callout " + ChatColor.LIGHT_PURPLE + callout + ChatColor.RESET + " for " + ChatColor.LIGHT_PURPLE + map.getName() + ChatColor.RESET + " map.");
                    map.getCallouts().remove(callout);
                    map.save();
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /map callout <add|remove> <map> <callout>");
            }
        }
        return true;
    }
}
