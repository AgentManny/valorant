package gg.manny.valorant.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemBuilder {
	
    private Material material;
    private int amount = 1;
    private short data = 0;
    private String display;
    private String[] lore;
    private String description;
    private String color;

    private ItemMeta meta;

    private Map<Enchantment, Integer> enchants = new HashMap<>();

    public ItemBuilder(ItemStack item) {
    	this.material = item.getType();
    	this.amount = item.getAmount();
    	this.data = (short)item.getData().getData();
    	if(item.getItemMeta() != null) {
    		if(item.getItemMeta().hasDisplayName()) {
    			this.display = item.getItemMeta().getDisplayName();
    		}
    		if(item.getItemMeta().hasLore()) {
    			this.lore = item.getItemMeta().getLore().toArray(new String[]{});
    		}
    	}
    }

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder() {
    	
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }


    public ItemBuilder material(Material mat) {
        this.material = mat;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(byte data) {
        this.data = data;
        return this;
    }

    public ItemBuilder data(int data) {
        this.data = (short) data;
        return this;
    }
    public ItemBuilder name(String name) {
        this.display = name;
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(Collection<String> lore) {
        this.lore = lore.toArray(new String[]{});
        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemBuilder description(String desc, String color) {
        this.description = desc;
        this.color = color;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder reset() {
        description = null;
        meta = null;
        lore = null;
        color = null;
        display = null;
        data = 0;
        amount = 1;
        material = null;
        return this;
    }

    public ItemStack build() {
        return this.create();
    }

    public ItemStack create() {
        final ItemStack item = new ItemStack(material, amount, data);

        if (meta != null) {
            item.setItemMeta(meta);
        } else {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(display);

            List<String> data = new ArrayList<>();

            if (lore != null) {
                for(String msg : lore) {
                	data.add(msg);
                }
            } else if (description != null) {
                data.addAll(wrap(description, color));
            }
            
            meta.setLore(data);
            item.setItemMeta(meta);

        }

        enchants.forEach(item::addUnsafeEnchantment);

        return item;
    }

    public static List<String> wrap(String string, String color, int length) {
        String[] split = string.split(" ");
        string = "";
        ArrayList<String> newString = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (string.length() > length || string.endsWith(".") || string.endsWith("!")) {
                newString.add(color + string);
                if (string.endsWith(".") || string.endsWith("!"))
                    newString.add("");
                string = "";
            }
            string += (string.length() == 0 ? "" : " ") + split[i];
        }
        newString.add(color + string);
        return newString;
    }


    public static List<String> wrap(String string, String color) {
        return wrap(string, color, 20);
    }

}