package me.zainmz.boosts.tasks;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.utils.Message;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheTask extends BukkitRunnable {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    private static final Yaml messsages = Boosts.messages;
    public HashMap<Integer, String> gBoost;

    public CacheTask(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoost = boosts.getgBoost();
    }

    @Override
    public void run() {

        //check if cache is empty and cancel task
        if(cache.isEmpty()){
            return;
        }


        //Deduct boost time value every 1 seconds
        for(Map.Entry<UUID, String> value: cache.entrySet()){
            String[] data = value.getValue().split(":");
            int time = Integer.parseInt(data[2]);
            time -= 1;

            //remove data if time is less than or equal to 0
            if(time <= 0){
                UUID uuid = value.getKey();
                Message.send("ended",messsages.getString("boost_end"),
                        null,null,null,value.getKey().toString(),null);
                cache.remove(uuid);
                return;
            }

            //update time value of user
            String newTime = String.valueOf(time);
            value.setValue(data[0] + ":" + data[1] + ":" + newTime);
        }
    }
}
