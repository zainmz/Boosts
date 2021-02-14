package me.zainmz.boosts.tasks;

import me.zainmz.boosts.Boosts;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class GlobalBoostTask extends BukkitRunnable {

    private final Boosts boosts;
    private final List<UUID> gBoostPlayers;
    private String gBoost;

    public GlobalBoostTask(Boosts boosts) {
        this.boosts = boosts;
        this.gBoost = boosts.getgBoost();
        this.gBoostPlayers = boosts.getgBoostPlayers();
    }

    @Override
    public void run() {

    if(gBoostPlayers.isEmpty()){
        this.cancel();
        return;
    }

    if(gBoost == null){
        System.out.println(gBoost);
        return;
    }

    //String[] getGlobalBoostTime = gBoost.split(":");
    //int time = Integer.parseInt(getGlobalBoostTime[2]);
    //time -= 1;
        //System.out.println(getGlobalBoostTime[2]);

    }
}
