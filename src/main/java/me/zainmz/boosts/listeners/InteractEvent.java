package me.zainmz.boosts.listeners;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InteractEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<String, ItemStack> items;
    private final HashMap<Integer, String> gBoost;
    private final HashMap<UUID, String> cache;
    private String itemKey;

    private static Yaml config = Boosts.configuration;

    public InteractEvent(Boosts boosts) {
        this.boosts = boosts;
        this.items = boosts.getItems();
        this.gBoost = boosts.getgBoost();
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(player.getInventory().getItemInMainHand() == null){
                return;
            }

            ItemStack hand = player.getInventory().getItemInMainHand();
            if(items.containsValue(hand)){

                //check if player already has personal boost active and stop
                if(cache.containsKey(player.getUniqueId())){
                    player.sendMessage(ChatColor
                            .translateAlternateColorCodes('&',"&cCannot use this since you already have a personal boost active!"));
                    return;
                }

                for(String key: items.keySet()){
                    if(hand.equals(items.get(key))){
                        itemKey = key;
                    }
                }

                //check if item has same type as global boost and stop
                if(!gBoost.isEmpty()){
                    String[] data = gBoost.get(1).split(":");
                    if(data[0].equalsIgnoreCase(config.getString("Items." + itemKey + ".type"))){
                        player.sendMessage(ChatColor
                                .translateAlternateColorCodes('&',"&cCannot use this since there is a global boost of this type active!"));
                        return;
                    }
                }


                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boost give " + player.getName() + " "
                        + config.getString("Items." + itemKey + ".type")  + " "
                        + config.getString("Items." + itemKey + ".multiplier")  + " "
                        + config.getString("Items." + itemKey + ".time") );

                player.getInventory().getItemInMainHand().setAmount(0);

            }
        }
    }
}
