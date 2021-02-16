package me.zainmz.boosts.items;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemCreator {

    private final Boosts boosts;
    private final HashMap<String, ItemStack> items;

    private static Yaml config = Boosts.configuration;

    public ItemCreator(Boosts boosts) {
        this.boosts = boosts;
        this.items = boosts.getItems();

    }

    public void build(){

        Set<String> keys = config.singleLayerKeySet("Items");
        ArrayList<String> itemNames = new ArrayList<>();
        for(String item: keys){
            itemNames.add(item);
        }

        for(String value: itemNames){

            //build items from config
            ItemStack item = new ItemStack(Material.getMaterial(config.getString("Items"+"."+value + "." + "item_type")),1);
            item.addUnsafeEnchantment(Enchantment.LUCK,1);

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor
                    .translateAlternateColorCodes('&',config.getString("Items"+"."+value + "." + "item_name")));
            itemMeta.setUnbreakable(true);

            //get lore from file and convert to colored lore
            ArrayList<String> coloredLore = new ArrayList<>();
            List<String> lore = config.getStringList("Items"+"."+value + "." + "lore");
            for(String lorevalue: lore){
                coloredLore.add(ChatColor.translateAlternateColorCodes('&',lorevalue));
            }
            itemMeta.setLore(coloredLore);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
            //add the item to the list of built items
            items.put(value,item);

        }

    }

}
