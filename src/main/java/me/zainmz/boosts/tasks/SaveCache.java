package me.zainmz.boosts.tasks;

import de.leonhard.storage.Yaml;
import me.zainmz.boosts.Boosts;

import java.util.*;

public class SaveCache {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    private static Yaml data = Boosts.data;
    public HashMap<Integer, String> gBoost;


    public SaveCache(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoost = boosts.getgBoost();
    }

    public void save(){


        if(!gBoost.isEmpty()){
            data.set(String.valueOf(1),gBoost.get(1));
            System.out.println("[Boosts] Saved existing global boost data!");
        }

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
