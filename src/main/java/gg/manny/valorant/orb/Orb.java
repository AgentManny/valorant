package gg.manny.valorant.orb;

import com.google.gson.JsonObject;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.util.MathUtil;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class Orb {

    private static double ORB_RADIUS = 1.5;
    private static long CAPTURE_TIME = TimeUnit.SECONDS.toMillis(3);

    private final OrbType type;
    private final Location location;

    private ArmorStand entity;
    private BukkitTask task;

    private long startedAt = -1;
    private boolean capturing = false;
    private Player playerCapturing = null;

    private boolean active = false;

    public Orb(JsonObject data) {
        this.type = OrbType.valueOf(data.get("type").getAsString());
        this.location = Valorant.GSON.fromJson(data.get("location"), Location.class);
    }

    public Orb(OrbType type, Location location) {
        this.type = type;
        this.location = location;
    }

    // Runs the animation every 1 tick
    public void runTask() {
        Location location = entity.getLocation();
        location.setYaw(location.getYaw() + 7);
        Bukkit.getScheduler().runTask(Valorant.getInstance(), () -> entity.teleport(location));
        ParticleEffect.FIREWORKS_SPARK.display(location, 0f, 1.3f, 0f, 0.1f, 1, null);

        List<Entity> nearbyEntities = entity.getNearbyEntities(ORB_RADIUS, ORB_RADIUS, ORB_RADIUS);
        if (playerCapturing != null && canCapture(playerCapturing) && nearbyEntities.contains(playerCapturing)) {
            if (startedAt + CAPTURE_TIME + 100L >= System.currentTimeMillis()) {
                long progress = System.currentTimeMillis() - startedAt;
                playerCapturing.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MathUtil.getProgressBar(progress, CAPTURE_TIME, 15, '\u25A0')));
            } else {
                end(playerCapturing, true);
            }
        } else if (capturing) {
            end(playerCapturing, false);
        } else {
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (canCapture(player)) {
                        start(player);
                    }
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "HOLD " + ChatColor.WHITE + "SNEAK" + ChatColor.GRAY + " TO CAPTURE ORB (+1 ULT POINT)"));
                }
            }
        }
    }

    private boolean canCapture(Player player) {
        return player.isOnline() && player.isSneaking() && player.isOnGround();
    }

    public void start(Player player) {
        this.capturing = true;
        this.playerCapturing = player;
        this.startedAt = System.currentTimeMillis();
        entity.setHelmet(new ItemStack(Material.BLACK_CONCRETE_POWDER));

        player.sendMessage("Started capturing..");
    }

    public void end(Player player, boolean finished) {
        if (finished) {
            player.sendMessage("Finished capturing orb.");
            setActive(false);
        } else {
            player.sendMessage("Cancelled capturing orb..");
        }

        entity.setHelmet(new ItemStack(Material.BLACK_CONCRETE));
        playerCapturing = null;
        capturing = false;
        startedAt = -1L;
    }

    private void create() {
        if (entity != null && !entity.isDead()) {
            entity.remove();
        }

        entity = location.getWorld().spawn(location, ArmorStand.class); // todo offset the location so it's centered?
        entity.setHelmet(new ItemStack(Material.BLACK_CONCRETE));

        // entity.setMarker(false); // Small collision box
        entity.setVisible(false);
        entity.setSmall(true);
        entity.setRemoveWhenFarAway(false);
        entity.setSilent(true);
        entity.setArms(true);
        entity.setGravity(false);
        entity.setBasePlate(false);
    }

    private void remove() {
        if (entity == null) return;
        if (task != null) {
            task.cancel();
            task = null;
        }
        entity.remove();
        entity = null;
    }

    public void setCapturing(boolean capturing) {
        if (active && entity != null && !entity.isDead()) {
            entity.setSmall(!capturing);
        }
        this.capturing = capturing;
    }

    public void setActive(boolean active) {
        if (!this.active && active) {
            capturing = false;
            create();
        } else if (this.active && !active) {
            remove();
        }
        this.active = active;
    }

    public JsonObject toJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", type.name());
        data.add("location", Valorant.GSON.toJsonTree(location));
        return data;
    }
}
