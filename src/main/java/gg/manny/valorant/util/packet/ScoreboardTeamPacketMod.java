package gg.manny.valorant.util.packet;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Collection;

public final class ScoreboardTeamPacketMod {

    private PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

    private static Field aField; // String
    private static Field bField; // Name (IChatComponent)
    private static Field cField; // Prefix (IChatComponent)
    private static Field dField; // Suffix (IChatComponent)
    private static Field eField; //
    private static Field fField;
    private static Field gField;

    /*
        private String a = "";
    private IChatBaseComponent b = new ChatComponentText("");
    private IChatBaseComponent c = new ChatComponentText("");
    private IChatBaseComponent d = new ChatComponentText("");
    private String e;
    private String f;
    private EnumChatFormat g;
    private final Collection<String> h;
    private int i;
    private int j;
     */

    static {
        try {
            aField = PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
            bField = PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
            cField = PacketPlayOutScoreboardTeam.class.getDeclaredField("c");
            dField = PacketPlayOutScoreboardTeam.class.getDeclaredField("d");
            eField = PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
            fField = PacketPlayOutScoreboardTeam.class.getDeclaredField("i");
            gField = PacketPlayOutScoreboardTeam.class.getDeclaredField("j");
            aField.setAccessible(true);
            bField.setAccessible(true);
            cField.setAccessible(true);
            dField.setAccessible(true);
            eField.setAccessible(true);
            fField.setAccessible(true);
            gField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ScoreboardTeamPacketMod(String name, String prefix, String suffix, Collection players, int paramInt) {
        try {
            aField.set(this.packet, name);
            fField.set(this.packet, paramInt);
            if (paramInt == 0 || paramInt == 2) {
                bField.set(this.packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + name + "\"}"));
                cField.set(this.packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + prefix + "\"}"));
                dField.set(this.packet, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + suffix + "\"}"));
                gField.set(this.packet, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paramInt == 0) {
            this.addAll(players);
        }
    }

    public ScoreboardTeamPacketMod(String name, Collection players, int paramInt) {
        try {
            gField.set(this.packet, 3);
            aField.set(this.packet, name);
            fField.set(this.packet, paramInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addAll(players);
    }

    public void sendToPlayer(Player bukkitPlayer) {
        ((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(this.packet);
    }

    private void addAll(Collection col) {
        if (col == null) {
            return;
        }

        try {
            ((Collection) eField.get(this.packet)).addAll(col);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

