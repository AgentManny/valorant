package gg.manny.valorant.util.serialization;

import com.google.gson.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;

import java.lang.reflect.Type;

public class CuboidRegionAdapter implements JsonDeserializer<CuboidRegion>, JsonSerializer<CuboidRegion> {

    @Override
    public CuboidRegion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement == null || !jsonElement.isJsonObject()) return null;

        JsonObject regionData = jsonElement.getAsJsonObject();

        World world = regionData.has("world") ? BukkitAdapter.adapt(Bukkit.getWorld(regionData.get("world").getAsString())) : null;

        JsonObject posOneData = regionData.get("pos1").getAsJsonObject();
        BlockVector3 pos1 = BlockVector3.at(posOneData.get("x").getAsInt(), posOneData.get("y").getAsInt(), posOneData.get("z").getAsInt());

        JsonObject posTwoData = regionData.get("pos2").getAsJsonObject();
        BlockVector3 pos2 = BlockVector3.at(posTwoData.get("x").getAsInt(), posTwoData.get("y").getAsInt(), posTwoData.get("z").getAsInt());


        CuboidRegion region = new CuboidRegion(world, pos1, pos2);
        return region;
    }

    @Override
    public JsonElement serialize(CuboidRegion region, Type type, JsonSerializationContext context) {
        if (region == null) return null;

        JsonObject regionData = new JsonObject();
        if (region.getWorld() != null) {
            regionData.addProperty("world", region.getWorld().getName());
        }

        JsonObject pos1 = new JsonObject();
        pos1.addProperty("x", region.getPos1().getX());
        pos1.addProperty("y", region.getPos1().getY());
        pos1.addProperty("z", region.getPos1().getZ());
        regionData.add("pos1", pos1);

        JsonObject pos2 = new JsonObject();
        pos2.addProperty("x", region.getPos2().getX());
        pos2.addProperty("y", region.getPos2().getY());
        pos2.addProperty("z", region.getPos2().getZ());
        regionData.add("pos2", pos2);
        return regionData;
    }
}
