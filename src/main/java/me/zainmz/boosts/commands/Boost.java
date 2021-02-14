package me.zainmz.boosts.commands;

import de.leonhard.storage.Yaml;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

@Command("boost")
public class Boost extends CommandBase {

    private final Boosts boosts;
    private final HashMap<String, String> cache;

    private static final Yaml messages = Boosts.messages;
    private static final Yaml data = Boosts.data;


    public Boost(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
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
            }
        }
    }

    @Permission("boost.give")
    @SubCommand("give")
    @Completion({"#players", "#types", "#range:1-5", "#empty"})
    @WrongUsage("/boost give <player> <type> <multiplier> <time in seconds>")
    public void giveCommand(final CommandSender sender, Player target,
                            String type, Double multi, Integer time) {

        if(sender instanceof Player){

            Player player = (Player) sender;
            beginBoost(player,
                    target,
                    type,
                    multi,
                    time,
                    "player");

        }else{
            beginBoost(null,
                    target,
                    type,
                    multi,
                    time,
                    "console");
        }

    }


    //check type and put on cache
    public void beginBoost(Player giver,Player target, String type, Double multi, Integer time, String sender){

        //check if target player already has a boost
        if(cache.containsKey(target.getUniqueId().toString())){
            if(giver != null){
                Message.send("cannot",messages
                        .getString("has_boost"),giver,target,time,type,multi);
                return;
            }
            System.out.println("[Boosts] You cannot boost " + target.getName()
                    + " as they already have an active boost");
                return;
        }

        //saves in format type:multi:time
        if(sender.equals("player")){
            cache.put(target.getUniqueId().toString(),type + ":" + multi + ":" + time);
            Message.send("given",messages
                    .getString("boost_given"),giver,target,time,type,multi);
            Message.send("giver",messages
                    .getString("boost_giver"),giver,target,time,type,multi);
            return;
        }

        if(sender.equals("console")){
            cache.put(target.getUniqueId().toString(),type + ":" + multi + ":" + time);
            System.out.println("[Boosts] Given "+ target.getName() + " " + type
                    + " boost for " + time +" seconds");

            Message.send("given",messages
                    .getString("boost_given"),giver,target,time,type,multi);

        }

    }

}
