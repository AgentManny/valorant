package gg.manny.valorant.map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.manny.valorant.Valorant;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapManager {

    protected static final File MAP_DIRECTORY;

    private final Valorant plugin;

    @Getter private List<GameMap> maps = new ArrayList<>();

    public MapManager(Valorant plugin) {
        this.plugin = plugin;

        if (MAP_DIRECTORY.exists()) {
            plugin.getLogger().info("[Map] Loading /maps/ directory...");
            File[] maps = MAP_DIRECTORY.listFiles((dir, name) -> name.endsWith(".json"));
            if (maps == null) {
                plugin.getLogger().info("[Map] No maps found.");
                return;
            }
            for (File mapFile : maps) {
                try (FileReader reader = new FileReader(mapFile)) {
                    JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    plugin.getLogger().info("[Map] Loaded map " + name + ".");
                    this.maps.add(new GameMap(name, jsonObject));

                } catch (Exception e) {
                    plugin.getLogger().severe("[Map] Failed to load " + mapFile.getName() + ":");
                    e.printStackTrace();
                }
            }
        } else {
            plugin.getLogger().info("[Map] Created /maps/ directory");
        }
    }

    static {
        MAP_DIRECTORY = new File(Valorant.getInstance().getDataFolder(), "maps");
        MAP_DIRECTORY.mkdirs();
    }

    public void save() {
        plugin.getLogger().info("[Map] Saving " + maps.size() + " maps...");
        maps.forEach(GameMap::save);
        plugin.getLogger().info("[Map] Saved " + maps.size() + " maps");
    }

    public GameMap getMapByName(String name) {
        for (GameMap map : maps) {
            if (map.getName().equalsIgnoreCase(name)) {
                return map;
            }
        }
        return null;
    }

}
