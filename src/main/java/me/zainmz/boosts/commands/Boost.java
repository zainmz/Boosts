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
import java.util.UUID;

@Command("boost")
public class Boost extends CommandBase {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    public HashMap<Integer, String> gBoost;

    private static final Yaml messages = Boosts.messages;
    private static final Yaml data = Boosts.data;


    public Boost(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
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


        if(target == null){
            Message.send("general",messages
                    .getString("No Player"),giver,target,time,type,multi);
            return;
        }

        if(time == null){
            Message.send("general",messages
                    .getString("No Time"),giver,target,time,type,multi);
            return;
        }

        //check if target player already has a boost
        if(cache.containsKey(target.getUniqueId())){
            if(giver != null){
                Message.send("cannot",messages
                        .getString("has_boost"),giver,target,time,type,multi);
                return;
            }
            System.out.println("[Boosts] You cannot boost " + target.getName()
                    + " as they already have an active boost");
                return;
        }


        if(sender.equals("player")){
            //check if global boost of the type entered is active
            if(!gBoost.isEmpty()){
                String[] data = gBoost.get(1).split(":");
                if(data[0].equalsIgnoreCase(type)){
                    Message.send("general",messages
                            .getString("boost_gboost_same_type"),giver,target,time,type,multi);
                    Message.send("target",messages
                            .getString("boost_gboost_same_type_pl"),giver,target,time,type,multi);
                    return;
                }
            }

            //saves in format type:multi:time
            cache.put(target.getUniqueId(),type + ":" + multi + ":" + time);
            Message.send("given",messages
                    .getString("boost_given"),giver,target,time,type,multi);
            Message.send("giver",messages
                    .getString("boost_giver"),giver,target,time,type,multi);
            return;
        }

        if(sender.equals("console")){
            if(!gBoost.isEmpty()){
                String[] data = gBoost.get(1).split(":");
                if(data[0].equalsIgnoreCase(type)){
                    System.out.println("[Boosts] Unable to give boost since a global boost of this type is active!");
                    Message.send("target",messages
                            .getString("boost_gboost_same_type_pl"),giver,target,time,type,multi);
                    return;
                }
            }
            //saves in format type:multi:time
            cache.put(target.getUniqueId(),type + ":" + multi + ":" + time);
            System.out.println("[Boosts] Given "+ target.getName() + " " + type
                    + " boost for " + time +" seconds");

            Message.send("given",messages
                    .getString("boost_given"),giver,target,time,type,multi);

        }

    }

}
