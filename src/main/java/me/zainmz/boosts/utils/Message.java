package me.zainmz.boosts.utils;

import me.zainmz.boosts.Boosts;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Message {


    public static void send(String msgType,String string, Player player,
                                   Player target, Integer time, String type, Double multi){

        switch(msgType){
            //send help message
            case "help":
                player.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string));
                return;

            //sends message to the player who is giving a boost
            case "giver":
                player.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string
                                .replace("%target%",target.getName())
                                .replace("%type%",type)
                                .replace("%multi%",multi.toString())
                                .replace("%time%",time.toString())));
                break;

            //sends message to the target
            case "given":
                target.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string
                                .replace("%type%",type)
                                .replace("%multi%",multi.toString())
                                .replace("%time%",time.toString())));
                return;

            //sends this if player already has a boost
            case "cannot":
                player.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string
                                .replace("%target%",target.getName())));
                return;

            //sends target that boost has ended
            case "ended":
                Player pl = Bukkit.getPlayer(UUID.fromString(type));
                if(pl != null){
                    pl.sendMessage(ChatColor
                            .translateAlternateColorCodes('&',string));
                }
                break;

            case "general":
                player.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string));
                break;

            case "target":
                target.sendMessage(ChatColor
                        .translateAlternateColorCodes('&',string));
                break;

            case "globalstart":
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',string
                        .replace("%type%",type)
                        .replace("%multi%",multi.toString())
                        .replace("%time%",time.toString())));
                break;
            default:
                return;
        }


    }

    public void gBoostEnd(){
        Bukkit.broadcastMessage(ChatColor
                .translateAlternateColorCodes('&',"&a&lBOOSTS &7Global boost has ended!"));
    }


}
