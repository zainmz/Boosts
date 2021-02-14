package me.zainmz.boosts.listeners;

import com.gamingmesh.jobs.container.CurrencyType;
import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.HashMap;
import java.util.UUID;

public class XpChangeEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;

    public XpChangeEvent(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onXpChangeEvent(PlayerExpChangeEvent event){

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(cache.containsKey(uuid)){
            if(cache.get(uuid).contains("xp")){
                String[] data = cache.get(uuid).split(":");
                double multi = Double.parseDouble(data[1]);
                int xp = event.getAmount();

                //getting new payment
                double newXP = xp + (xp * multi);
                event.setAmount((int) newXP);

            }
        }
    }
}