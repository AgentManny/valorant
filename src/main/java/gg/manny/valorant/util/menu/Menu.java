package gg.manny.valorant.util.menu;

import gg.manny.valorant.Valorant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public abstract class Menu {

    private static Method openInventoryMethod;
    private ConcurrentHashMap<Integer, Button> buttons = new ConcurrentHashMap<>();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean placeholder = false;
    private boolean noncancellingInventory = false;
    @NonNull private String staticTitle = null;
    public static Map<String, Menu> currentlyOpenedMenus = new HashMap<>();
    public static Map<String, BukkitRunnable> checkTasks = new HashMap<>();

    static {
        Valorant.getInstance().getServer().getPluginManager().registerEvents(new ButtonListener(), Valorant.getInstance());
    }

    private Inventory createInventory(Player player) {
        Map<Integer, Button> invButtons = this.getButtons(player);
        Inventory inv = Bukkit.createInventory(player, this.size(invButtons), this.getTitle(player));
        for (Map.Entry<Integer, Button> buttonEntry : invButtons.entrySet()) {
            this.buttons.put(buttonEntry.getKey(), buttonEntry.getValue());
            inv.setItem(buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));
        }
        if (this.isPlaceholder()) {
            Button placeholder = Button.placeholder(Material.GRAY_STAINED_GLASS_PANE);
            for (int index = 0; index < this.size(invButtons); ++index) {
                if (invButtons.get(index) != null) continue;
                this.buttons.put(index, placeholder);
                inv.setItem(index, placeholder.getButtonItem(player));
            }
        }
        return inv;
    }

    private static Method getOpenInventoryMethod() {
        if (openInventoryMethod == null) {
            try {

                openInventoryMethod = CraftHumanEntity.class.getDeclaredMethod("openCustomInventory", Inventory.class, EntityPlayer.class, Containers.class);
                openInventoryMethod.setAccessible(true);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return openInventoryMethod;
    }

    public void openMenu(Player player) {
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        Inventory inv = this.createInventory(player);
        try {
            Containers customSize;
            switch(inv.getSize()) {
                case 9:
                    customSize = Containers.GENERIC_9X1;
                    break;
                case 18:
                    customSize = Containers.GENERIC_9X2;
                    break;
                case 27:
                    customSize = Containers.GENERIC_9X3;
                    break;
                case 36:
                case 41:
                    customSize = Containers.GENERIC_9X4;
                    break;
                case 45:
                    customSize = Containers.GENERIC_9X5;
                    break;
                case 54:
                    customSize = Containers.GENERIC_9X6;
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported custom inventory size " + inv.getSize());
            }
            getOpenInventoryMethod().invoke(player, inv, ep, customSize);
            this.update(player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void update(final Player player) {
        cancelCheck(player);
        currentlyOpenedMenus.put(player.getName(), this);
        this.onOpen(player);
        BukkitRunnable runnable = new BukkitRunnable() {

            public void run() {
                if (!player.isOnline()) {
                    cancelCheck(player);
                    currentlyOpenedMenus.remove(player.getName());
                }
                if (Menu.this.isAutoUpdate()) {
                    player.getOpenInventory().getTopInventory().setContents(Menu.this.createInventory(player).getContents());
                }
            }
        };
        runnable.runTaskTimer(Valorant.getInstance(), 10L, 10L);
        checkTasks.put(player.getName(), runnable);
    }

    public static void cancelCheck(Player player) {
        if (checkTasks.containsKey(player.getName())) {
            checkTasks.remove(player.getName()).cancel();
        }
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;
        for (int buttonValue : buttons.keySet()) {
            if (buttonValue <= highest) continue;
            highest = buttonValue;
        }
        return (int) (Math.ceil((double) (highest + 1) / 9.0) * 9.0);
    }

    public int getSlot(int x, int y) {
        return 9 * y + x;
    }

    public String getTitle(Player player) {
        return this.staticTitle;
    }

    public abstract Map<Integer, Button> getButtons(Player var1);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }
    
}

