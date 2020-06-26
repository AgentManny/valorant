package gg.manny.valorant.ability.sage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gg.manny.valorant.Valorant;
import gg.manny.valorant.ability.Ability;
import gg.manny.valorant.util.MathUtil;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;
import org.ipvp.ingot.ActionHandler;
import org.ipvp.ingot.HotbarAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SlowOrb extends Ability {

    private Multimap<UUID, Integer> dummyEntities = ArrayListMultimap.create();

    public SlowOrb() {
        super("Slow Orb", AbilityType.BASIC, ChatColor.AQUA);
    }

    @Override
    public int getCost() {
        return 100;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public Material getIcon() {
        return Material.BLUE_ICE;
    }

    @Override
    public String getDescription() {
        return "Equip a Slowing Orb. " +
                "Fire to launch the Orb, which expands upon hitting the ground, creating a zone that slows players who walk on it.";
    }

    @Override
    public int getSlot() {
        return 4;
    }

    @Override
    public boolean activate(Player player, HotbarAction action) {
        // todo Check if they have enough money to buy or if they purchased any for game starting
        if (action.getType() == ActionHandler.ActionType.RIGHT_CLICK) {
            Snowball projectile = player.launchProjectile(Snowball.class);
            projectile.setMetadata(getName(), new FixedMetadataValue(Valorant.getInstance(), player.getUniqueId().toString())); // Store UUID for faster access we could also use Projectile#getShooter
            return true;
        } else if (action.getType() == ActionHandler.ActionType.LEFT_CLICK) {
            List<Integer> entityIds = new ArrayList<>();
            Location loc = player.getLocation().clone().subtract(0, 1, 0);
            for (int i = 0; i < 5; i++) {
                addWeirdArmorStand(player, loc);
            }

            int slowCooldown = 5;
            Valorant.getInstance().getServer().getScheduler().runTaskLater(Valorant.getInstance(), () -> {
                for (Player onlinePlayer : player.getWorld().getPlayers()) {
                    entityIds.forEach(id -> {
                        ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(id));
                    });
                }
            }, 20L * slowCooldown);
        }

        return false;
    }

    public int addWeirdArmorStand(Player player, Location landLocation) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        EntityArmorStand entityArmorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, entityPlayer.world);
        entityArmorStand.setInvisible(true);
        entityArmorStand.setFlag(5, true);
        entityArmorStand.setSmall(true);
        entityArmorStand.setNoGravity(true);
        entityArmorStand.setArms(true);

        // This will make the floor look more spikey (ziqr's dog. cool)
        entityArmorStand.setHeadPose(new Vector3f((float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360)), (float) (Valorant.RANDOM.nextInt(360))));
        entityArmorStand.setLocation(landLocation.getX() + MathUtil.randomDouble(-1.5, 1.5), landLocation.getY() + MathUtil.randomDouble(0, .5) - 0.75, landLocation.getZ() + MathUtil.randomDouble(-1.5, 1.5), 0, 0);
        dummyEntities.put(player.getUniqueId(), entityArmorStand.getId());

        for (Player onlinePlayer : player.getWorld().getPlayers()) {
            Arrays.asList(
                    new PacketPlayOutSpawnEntityLiving(entityArmorStand),
                    new PacketPlayOutEntityMetadata(entityArmorStand.getId(), entityArmorStand.getDataWatcher(), false),
                    new PacketPlayOutEntityEquipment(entityArmorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(org.bukkit.Material.PACKED_ICE)))
            ).forEach(packet -> ((CraftPlayer)onlinePlayer).getHandle().playerConnection.sendPacket(packet));
        }

        return entityArmorStand.getId();
    }
}
