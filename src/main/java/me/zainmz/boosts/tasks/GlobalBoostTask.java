package me.zainmz.boosts.tasks;

import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.utils.Message;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GlobalBoostTask extends BukkitRunnable {

    private final Boosts boosts;
    private final List<UUID> gBoostPlayers;
    public HashMap<Integer, String> gBoost;
    private final Message message;

    public GlobalBoostTask(Boosts boosts) {
        this.boosts = boosts;
        this.gBoost = boosts.getgBoost();
        this.gBoostPlayers = boosts.getgBoostPlayers();
        this.message = boosts.getMessage();
    }

    @Override
    public void run() {

    if(gBoostPlayers.isEmpty()){
        System.out.println("[Boosts] No players online , cancelling...");
        gBoost.remove(1);
        this.cancel();
        return;
    }

    if(gBoost.isEmpty()){
        System.out.println("[Boosts] No global boost available , cancelling...");
        this.cancel();
        return;
    }


    //deduct seconds from time of global boost
    String[] getGlobalBoostTime = gBoost.get(1).split(":");
    int time = Integer.parseInt(getGlobalBoostTime[2]);
    time -= 1;

    //check if time is over and end global boost
    if(time <= 0){
        System.out.println("[Boosts] Global boost has finished!");
        gBoost.remove(1);
        gBoostPlayers.clear();
        this.cancel();
        return;
    }

    //update time value in hashmap
    gBoost.put(1,getGlobalBoostTime[0]+":"+getGlobalBoostTime[1]+":"+String.valueOf(time));

    }
}
