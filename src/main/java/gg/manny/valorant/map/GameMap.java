package gg.manny.valorant.map;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import gg.manny.valorant.Valorant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static gg.manny.valorant.map.MapManager.MAP_DIRECTORY;

@Getter
public class GameMap {

    /** Returns the name of the map */
    private final String name;

    /** Returns the description of a map, if it has any */
    @Setter
    private String description = "";

    /** Returns the location areas of a game map */
    private Map<String, Polygonal2DRegion> locations = new HashMap<>();

    public GameMap(String name) {
        this.name = name;
        save();
    }

    public GameMap(String name, JsonObject data) {
        this.name = name;
        this.description = data.get("description").getAsString();

        if (data.has("locations") && data.get("locations").isJsonObject()) {
            JsonObject locations = data.getAsJsonObject("locations");
            locations.entrySet().forEach(entry -> this.locations.put(entry.getKey(), Valorant.GSON.fromJson(entry.getValue(), new TypeToken<Polygonal2DRegion>(){}.getType())));
        }
    }

    // So we'll create a folder name "maps", the map will contain a JSON file of the map name
    // e.g. bind.json (lowercase name)
    // inside the json would be:
    // name, description, locations (another object)
    public void save() {
        File file = new File(MAP_DIRECTORY, name.toLowerCase().replace(" ", "_") + ".json");
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
        data.addProperty("description", description);

        JsonObject locations = new JsonObject();
        this.locations.forEach((key, region) -> locations.add(key, Valorant.GSON.toJsonTree(region)));

        data.add("locations", locations);
        return data;
    }


}
