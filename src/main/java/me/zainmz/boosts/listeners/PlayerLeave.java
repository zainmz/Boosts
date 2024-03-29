package me.zainmz.boosts.listeners;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerLeave implements Listener {

    private final Boosts boosts;
    private final Yaml data = Boosts.data;
    private final HashMap<UUID, String> cache;
    private final List<UUID> gBoostPlayers;
    public HashMap<Integer, String> gBoost;

    public PlayerLeave(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoost = boosts.getgBoost();
        this.gBoostPlayers = boosts.getgBoostPlayers();
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event){

        UUID uuid = event.getPlayer().getUniqueId();

        //remove player from global boost list
        if(!gBoost.isEmpty()){
            gBoostPlayers.remove(uuid);
        }


        //check if player has boost, save and remove from map
        if(cache.containsKey(uuid)){
            data.set(uuid.toString(),cache.get(uuid));
            System.out.println("[Boosts] Saving boost data for " + uuid);
            cache.remove(uuid);
        }

    }




}
