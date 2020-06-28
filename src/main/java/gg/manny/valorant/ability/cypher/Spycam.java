package gg.manny.valorant.ability.cypher;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import gg.manny.valorant.ability.Ability;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.lang.reflect.Method;
import java.util.*;

public class Spycam extends Ability {

    private static int CAMERA_RADIUS = 20;

    private Map<UUID, EntityArmorStand> hovering = new HashMap<>();

    public Spycam() {
        super("Spycam", AbilitySkill.SIGNATURE, AbilityPrice.FREE);
    }

    @Override
    public Material getIcon() {
        return Material.CAKE;
    }

    @Override
    public String getDescription() {
        return "Equip a spycam. Fire to place the spycam at the targeted location. " +
                "REUSE this ability to take control of the camera's view. While in control of the camera, Fire to shoot a tracking dart. " +
                "The dart is removable and will periodically reveal the location of the enemy hit.";
    }

    @Override
    public void tick(Player player) {
        if (hovering.containsKey(player.getUniqueId())) {
            EntityArmorStand entity = hovering.get(player.getUniqueId());
            updateLocation(player, entity);
        }
        player.sendMessage("Looking at: " + player.getLocation().getPitch());
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        ActionHandler.ActionType actionType = action.getType();
        if (actionType == ActionHandler.ActionType.HOVER) {
            hovering.put(player.getUniqueId(), getCoolStand(player));
            player.sendMessage(ChatColor.GRAY + "DEBUG: Cypher Mode activated");
        } else if (actionType == ActionHandler.ActionType.UNHOVER) {
            EntityArmorStand entity = hovering.get(player.getUniqueId());
            hovering.remove(player.getUniqueId());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()));
            player.sendMessage(ChatColor.GRAY + "DEBUG: Cypher Mode de-activated");
        }



        return false;
    }

    public EntityArmorStand getCoolStand(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, entityPlayer.world);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setFlag(5, true);
        entityArmorStand.setSmall(true);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setArms(true);

//        DataWatcher dataWatcher = entityArmorStand.getDataWatcher();
//        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
        // This will make the floor look more spikey (ziqr's dog. cool)
       //  entityArmorStand.setHeadPose(new Vector3f((float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360))));
        updateLocation(player, entityArmorStand);

        Arrays.asList(
                new PacketPlayOutSpawnEntityLiving(entityArmorStand),
                new PacketPlayOutEntityMetadata(entityArmorStand.getId(), entityArmorStand.getDataWatcher(), false),
                new PacketPlayOutEntityEquipment(entityArmorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.BEDROCK)))
        ).forEach(packet -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet));
        return entityArmorStand;
    }

    private void updateLocation(Player player, EntityArmorStand armorStand) {
        List<Block> blocks = player.getLastTwoTargetBlocks(null, CAMERA_RADIUS);
        Block block = player.getTargetBlockExact(CAMERA_RADIUS);
        if (blocks.isEmpty()) return;

            Location location = blocks.get(0).getLocation();
            boolean accessible = block != null && blocks.get(0).getType() == Material.AIR && player.getLocation().getPitch() < -15;

            Vector facingVector = player.getLocation().toVector().subtract(location.toVector()).normalize(); // We make da armor stand look at da nigga
            location.setDirection(facingVector);

            ItemStack cameraHead = new ItemStack(accessible ? Material.PLAYER_HEAD : Material.RED_GLAZED_TERRACOTTA);
            if (accessible) {

                SkullMeta meta = (SkullMeta) cameraHead.getItemMeta();

                GameProfile profile = new GameProfile(UUID.randomUUID(), null);

                String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWIyOGY0ZWVmZjg5MWI3OGQ1MWY3NWQ4NzIyYzYyODQ4NGJhNDlkZjljOWYyMzcxODk4YzI2OTY3Mzg2In19fQ";

                profile.getProperties().put("textures", new Property("textures", texture));

                try {
                    Method method = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                    method.setAccessible(true);
                    method.invoke(meta, profile);
                } catch (Exception ignored) { }

                cameraHead.setItemMeta(meta);
            }
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityEquipment(armorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(cameraHead)));

            armorStand.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityTeleport(armorStand));

    }

}
