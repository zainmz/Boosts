package me.zainmz.boosts.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class JobsXpGainEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<String, String> cache;

    public JobsXpGainEvent(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onJobsXpGain(JobsExpGainEvent event){

        Player player = (Player) event.getPlayer();
        String uuid = player.getUniqueId().toString();

        if(cache.containsKey(player.getUniqueId().toString())){
            if(cache.get(uuid).contains("jobsxp")){
                String[] data = cache.get(uuid).split(":");
                double multi = Double.parseDouble(data[1]);
                double xp = event.getExp();

                //getting new payment
                double newXp = xp + (xp * multi);
                event.setExp(newXp);
                player.sendMessage(xp + " " + newXp);

            }
        }
    }
}
