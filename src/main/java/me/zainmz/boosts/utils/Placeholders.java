package me.zainmz.boosts.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.zainmz.boosts.Boosts;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Placeholders extends PlaceholderExpansion {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    private final List<UUID> gBoostPlayers;
    private final PrettyTime prettyTime;
    public HashMap<Integer, String> gBoost;


    public Placeholders(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoost = boosts.getgBoost();
        this.gBoostPlayers = boosts.getgBoostPlayers();
        this.prettyTime = new PrettyTime();
    }


    @Override
    public boolean canRegister(){
        return true;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "boosts";
    }

    @Override
    public @NotNull String getAuthor() {
        return "zainmz";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        UUID uuid = player.getUniqueId();
        // %boosts_player% - returns player's current boost type
        if(identifier.equals("player")){
            if(cache.containsKey(uuid)){
                String[] data = cache.get(uuid).split(":");
                return data[0];
            }
            return "Inactive";

        }

        // %boosts_player_multiplier% - returns player's current boost multiplier
        if(identifier.equals("player_multiplier")){
            if(cache.containsKey(uuid)){
                String[] data = cache.get(uuid).split(":");
                return String.valueOf(data[1]);
            }
            return "";

        }

        // %boosts_player_time% - returns players personal boost remaining time
        if(identifier.equals("player_time")){

            if(cache.containsKey(uuid)){
                String[] data = cache.get(uuid).split(":");
                return prettyTime.format(LocalDateTime.now().plusSeconds(Long.parseLong(data[2])));
            }
            return "";
        }

        // %boosts_global% - returns global boost type
        if(identifier.equals("global")){

            if(!gBoost.isEmpty()){
                String[] data = gBoost.get(1).split(":");
                return data[0];
            }
            return "Inactive";
        }

        // %boosts_global_time% - returns global boost multiplier
        if(identifier.equals("global_multiplier")){
            if(gBoost.containsKey(1)){
                String[] data = gBoost.get(1).split(":");
                return String.valueOf(data[1]);
            }
            return "";

        }

        // %boosts_global_time% - returns global boost remaining time
        if(identifier.equals("global_time")){

            if(gBoost.containsKey(1)){
                String[] data = gBoost.get(1).split(":");
                return prettyTime.format(LocalDateTime.now().plusSeconds(Long.parseLong(data[2])));
            }
            return "";
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return "";
    }
}

