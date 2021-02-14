package me.zainmz.boosts.listeners;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class JobsPayEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<String, String> cache;

    public JobsPayEvent(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onPayEvent(JobsPaymentEvent event){

        Player player = (Player) event.getPlayer();
        String uuid = player.getUniqueId().toString();

        //check if cache has event player with jobs boost
        if(cache.containsKey(player.getUniqueId().toString())){
            if(cache.get(uuid).contains("jobspay")){
                String[] data = cache.get(uuid).split(":");
                double multi = Double.parseDouble(data[1]);
                double pay = event.get(CurrencyType.MONEY);

                //getting new payment
                double newPay = pay + (pay * multi);
                event.set(CurrencyType.MONEY, newPay);

            }
        }
    }
}