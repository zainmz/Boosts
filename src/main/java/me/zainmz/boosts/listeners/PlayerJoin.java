package me.zainmz.boosts.listeners;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerJoin implements Listener {

    private final Boosts boosts;
    private final Yaml data = Boosts.data;
    private final HashMap<UUID, String> cache;
    private final List<UUID> gBoostPlayers;
    public HashMap<Integer, String> gBoost;

    public PlayerJoin(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoostPlayers = boosts.getgBoostPlayers();
        this.gBoost = boosts.getgBoost();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){

        //load boost data on join if exists
        UUID uuid = event.getPlayer().getUniqueId();

        //if global boost is running add player to global boost
        if(!gBoost.isEmpty()){
            gBoostPlayers.add(uuid);
        }

        //Load player boost data if exists
        if(data.getString(uuid.toString()).contains(":")){
            cache.put(uuid,data.getString(uuid.toString()));
            System.out.println("[Boosts] Loading boost data for " + uuid);
            data.remove(uuid.toString());
        }

    }
}
