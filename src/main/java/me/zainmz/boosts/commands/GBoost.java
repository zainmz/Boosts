package me.zainmz.boosts.commands;

import de.leonhard.storage.Yaml;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.tasks.GlobalBoostTask;
import me.zainmz.boosts.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Command("gboost")
public class GBoost extends CommandBase {

    private final Boosts boosts;
    private final List<UUID> gBoostPlayers;
    public HashMap<Integer, String> gBoost;
    private BukkitTask bukkitTask;

    private static final Yaml messages = Boosts.messages;

    public GBoost(Boosts boosts) {
        this.boosts = boosts;
        this.gBoostPlayers = boosts.getgBoostPlayers();
        this.gBoost = boosts.getgBoost();
    }

    @Default
    public void defaultCommand(final CommandSender sender, final String[] args) {

        //sends default usage to player
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                List<String> help = messages.getStringList("Usage");
                for (String value : help) {
                    Message.send("help",value,player,null,null,null,null);
                }
                player.sendMessage(gBoost.toString());
                player.sendMessage(gBoostPlayers.toString());
            }
        }
    }

    @Permission("boost.start.global")
    @SubCommand("start")
    @Completion({"#types", "#range:1-5", "#empty"})
    @WrongUsage("/gboost start <type> <multiplier> <time in seconds>")
    public void giveCommand(final CommandSender sender,
                            String type, Double multi, Integer time) {

        if(sender instanceof Player){
            Player player = (Player) sender;
            startGlobalBoost(player,
                            type,
                            multi,
                            time,
                            "player");
        }else{
            startGlobalBoost(null,
                    type,
                    multi,
                    time,
                    "console");
        }

    }

    public void startGlobalBoost(Player giver, String type, Double multi, Integer time, String sender){

        //check if a global boost is active
        if(!gBoost.isEmpty()){
            if(sender.equals("player")){
                Message.send("general",messages.getString("gboost_active"),giver
                        ,null,null,null,null);
                return;
            }
            System.out.println("[Boosts] A global boost is already active!");
            return;
        }


        if(sender.equals("player")){
            gBoost.put(1,type + ":" + multi + ":" + time);
            for(Player player: Bukkit.getOnlinePlayers()){
                gBoostPlayers.add(player.getUniqueId());
            }
            Message.send("globalstart",messages.getString("gboost_start"),
                        giver,null,time,type,multi);
            bukkitTask = new GlobalBoostTask(boosts).runTaskTimerAsynchronously(boosts,20L,20L);
            return;

        }

        if(sender.equals("console")){
            gBoost.put(1,type + ":" + multi + ":" + time);
            for(Player player: Bukkit.getOnlinePlayers()){
                gBoostPlayers.add(player.getUniqueId());
            }
            System.out.println("[Boosts] Started a global " + type
                    + " boost with multiplier " + multi + " for " + time + " seconds");
            bukkitTask = new GlobalBoostTask(boosts).runTaskTimerAsynchronously(boosts,20L,20L);
            return;
        }

    }

}
