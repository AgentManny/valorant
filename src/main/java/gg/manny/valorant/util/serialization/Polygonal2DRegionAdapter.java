package gg.manny.valorant.util.serialization;

import com.google.gson.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;

public class Polygonal2DRegionAdapter implements JsonDeserializer<Polygonal2DRegion>, JsonSerializer<Polygonal2DRegion> {

    @Override
    public Polygonal2DRegion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement == null || !jsonElement.isJsonObject()) return null;

        JsonObject regionData = jsonElement.getAsJsonObject();

        World world = regionData.has("world") ? BukkitAdapter.adapt(Bukkit.getWorld(regionData.get("world").getAsString())) : null;

        Polygonal2DRegion region = new Polygonal2DRegion(world);

        region.setMinimumY(regionData.get("minY").getAsInt());
        region.setMaximumY(regionData.get("maxY").getAsInt());

        if (regionData.has("points")) {
            JsonArray pointsData = regionData.getAsJsonArray("points");
            pointsData.forEach(data -> {
                JsonObject pointData = data.getAsJsonObject();
                region.addPoint(BlockVector2.at(pointData.get("x").getAsInt(), pointData.get("z").getAsInt()));
            });
        }
        return region;
    }

    @Override
    public JsonElement serialize(Polygonal2DRegion region, Type type, JsonSerializationContext context) {
        if (region == null) return null;

        JsonObject regionData = new JsonObject();
        if (region.getWorld() != null) {
            regionData.addProperty("world", region.getWorld().getName());
        }

        regionData.addProperty("minY", region.getMinimumY());
        regionData.addProperty("maxY", region.getMaximumY());

        JsonArray pointsData = new JsonArray();
        region.getPoints().forEach(point -> {
            JsonObject pointData = new JsonObject();
            pointData.addProperty("x", point.getX());
            pointData.addProperty("z", point.getZ());
            pointsData.add(pointData);
        });
        regionData.add("points", pointsData);

        return regionData;
    }
}
