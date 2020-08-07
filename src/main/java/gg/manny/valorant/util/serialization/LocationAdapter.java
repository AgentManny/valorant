package gg.manny.valorant.util.serialization;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationAdapter implements JsonDeserializer<Location>, JsonSerializer<Location> {

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement == null || !jsonElement.isJsonObject()) return null;

        JsonObject locationData = jsonElement.getAsJsonObject();

        World world = Bukkit.getWorld(locationData.get("world").getAsString());
        double x = locationData.get("x").getAsDouble();
        double y = locationData.get("y").getAsDouble();
        double z = locationData.get("z").getAsDouble();
        float pitch = locationData.has("pitch") ? locationData.get("pitch").getAsFloat() : 0;
        float yaw = locationData.has("yaw") ? locationData.get("yaw").getAsFloat() : 0;

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        if (location == null) return null;

        JsonObject locationData = new JsonObject();
        locationData.addProperty("world", location.getWorld().getName());

        locationData.addProperty("x", location.getX());
        locationData.addProperty("y", location.getY());
        locationData.addProperty("z", location.getZ());

        // Some data that is serialized doesn't use the YAW/PITCH and I want the serialized to be as compact as possible
        if (location.getYaw() != 0) {
            locationData.addProperty("yaw", location.getYaw());
        }

        if (location.getPitch() != 0) {
            locationData.addProperty("pitch", location.getPitch());
        }
        return locationData;

    }
}
