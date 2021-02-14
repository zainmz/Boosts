package me.zainmz.boosts.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class JobsXpGainEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;

    public JobsXpGainEvent(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onJobsXpGain(JobsExpGainEvent event){

        Player player = (Player) event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(cache.containsKey(uuid)){
            if(cache.get(uuid).contains("jobsxp")){
                String[] data = cache.get(uuid).split(":");
                double multi = Double.parseDouble(data[1]);
                double xp = event.getExp();

                //getting new payment
                double newXp = xp + (xp * multi);
                event.setExp(newXp);

            }
        }
    }
}
