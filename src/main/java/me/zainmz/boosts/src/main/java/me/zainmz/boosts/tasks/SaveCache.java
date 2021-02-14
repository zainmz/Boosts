package me.zainmz.boosts.tasks;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;

import java.util.*;

public class SaveCache {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    private static Yaml data = Boosts.data;


    public SaveCache(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    public void save(){


        if(!cache.isEmpty()){

            //add all data on cache to string list
            // UUID: value
            for(Map.Entry<UUID, String> value: cache.entrySet()){
                data.set(value.getKey().toString(),value.getValue());
            }

            //save list onto data file
            System.out.println("[Boosts] Saved existing boost data!");

        }
    }
}
