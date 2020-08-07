package gg.manny.valorant.map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.game.TeamType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gg.manny.valorant.map.MapManager.MAP_DIRECTORY;

@Getter
public class GameMap {

    /** Returns the name of the map */
    private final String name;

    /** Returns the author of the map */
    @Setter private String author = "Unknown";

    /** Returns the description of the map */
    @Setter private String description = "";

    /** Returns all locations of ultimate orbs */
    private List<Location> orbs = new ArrayList<>();

    /** Returns the spawn locations for both teams */
    private Multimap<TeamType, Location> spawnPoints = ArrayListMultimap.create();

    /** Returns region of where the Spike (Bomb) can be planted */
    private List<CuboidRegion> plantSites = new ArrayList<>();

    /** Returns the callout areas defined within a map */
    private Map<String, Polygonal2DRegion> callouts = new HashMap<>();

    public GameMap(String name) {
        this.name = name;
        save();
    }

    public GameMap(String name, JsonObject data) {
        this.name = name;
        this.description = data.get("description").getAsString();
        this.author = data.get("author").getAsString();

        if (data.has("locations") && data.get("locations").isJsonObject()) {
            JsonObject locations = data.getAsJsonObject("locations");
            locations.entrySet().forEach(entry -> this.callouts.put(entry.getKey(), Valorant.GSON.fromJson(entry.getValue(), Polygonal2DRegion.class)));
        }
    }

    public String getCalloutByLocation(Location location) {
        for (Map.Entry<String, Polygonal2DRegion> entry : callouts.entrySet()) {
            String callout = entry.getKey();
            Polygonal2DRegion region = entry.getValue();
            if (region.contains(BlockVector3.at(location.getX(), location.getY(), location.getZ()))) {
                return callout;
            }
        }
        return null;
    }

    // So we'll create a folder name "maps", the map will contain a JSON file of the map name
    // e.g. bind.json (lowercase name)
    // inside the json would be:
    // name, description, locations (another object)
    public void save() {
        File file = getFile();
        try (FileWriter writer = new FileWriter(file)) {
            Valorant.GSON.toJson(getJson(), writer);
        } catch (IOException e) {
            Valorant.getInstance().getLogger().severe("[Map] Failed to save " + file.getName() + ":");
            e.printStackTrace();
            return;
        }

        Valorant.getInstance().getLogger().info("[Map] Saved " + name);
        // Save to disk
    }

    private JsonObject getJson() {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("author", author);
        data.addProperty("description", description);

        JsonObject locations = new JsonObject();
        this.callouts.forEach((key, region) -> locations.add(key, Valorant.GSON.toJsonTree(region)));
        data.add("locations", locations);
        return data;
    }

    public File getFile() {
        return new File(MAP_DIRECTORY, name.toLowerCase().replace(" ", "_") + ".json");
    }
}
