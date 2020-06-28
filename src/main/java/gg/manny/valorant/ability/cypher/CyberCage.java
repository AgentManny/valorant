package gg.manny.valorant.ability.cypher;

import com.mojang.authlib.GameProfile;
import gg.manny.valorant.ability.Ability;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.util.Arrays;
import java.util.UUID;

public class CyberCage extends Ability {

    public CyberCage() {
        super("Cyber Cage", AbilitySkill.BASIC, AbilityPrice.CREDITS);
    }

    @Override
    public Material getIcon() {
        return Material.IRON_BARS;
    }

    @Override
    public String getDescription() {
        return "Toss out a remote activation trap. " +
                "Reactivate to create a cage that slows enemies who pass through it. " +
                "Look at a trap and press USE to detonate it, or hold ACTIVATE to detonate all.";
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK) {
            getCoolStand(player);
            return true;
        }
        return false;
    }

    public EntityPlayer getCoolStand(Player player) {
        player.setCollidable(false);
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();

        EntityPlayer entity = new EntityPlayer(minecraftServer, worldServer, new GameProfile(UUID.randomUUID(), "CyberCage"), new PlayerInteractManager(worldServer));
        entity.playerConnection = new PlayerConnection(minecraftServer, new NetworkManager(EnumProtocolDirection.CLIENTBOUND), entity);
        int entityID = entity.getId();

        entity.setInvisible(true);
        entity.setInvulnerable(true);
        CraftPlayer player1 = (CraftPlayer)player;
        Location loc = player.getLocation();
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 90);

        DataWatcher dataWatcher = entity.getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);

        Arrays.asList(
                new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entity),
                new PacketPlayOutNamedEntitySpawn(entity),
                new PacketPlayOutEntityMetadata(entityID, dataWatcher, true)
        ).forEach(packet -> ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet));
        return entity;
    }
}
