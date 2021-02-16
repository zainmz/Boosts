package me.zainmz.boosts.listeners;

import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CraftEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<String, ItemStack> items;

    public CraftEvent(Boosts boosts) {
        this.boosts = boosts;
        this.items = boosts.getItems();
    }

    @EventHandler
    public void onCraftEvent(PrepareItemCraftEvent event){

        CraftingInventory inv = event.getInventory();
        for(ItemStack item: inv){
            if(items.containsValue(item)){
                inv.setResult(null);
            }
        }
    }
}
