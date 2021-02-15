package me.zainmz.boosts.commands;

import de.leonhard.storage.Yaml;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Command("boost")
public class Cancel extends CommandBase {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;
    public HashMap<Integer, String> gBoost;

    private static final Yaml messages = Boosts.messages;
    private static final Yaml data = Boosts.data;


    public Cancel(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
        this.gBoost = boosts.getgBoost();
    }

    @Permission("boost.cancel.player")
    @SubCommand("cancelp")
    @Completion({"#players"})
    @WrongUsage("/boost cancelp <player>")
    public void giveCommand(final CommandSender sender, Player target) {

        if(sender instanceof Player){

            Player player = (Player) sender;
            cancelPlayerBoost(player,
                            target,
                            "player");

        }else{
            cancelPlayerBoost(null,
                    target,
                    "console");
        }

    }

    @Permission("boost.cancel.global")
    @SubCommand("cancelg")
    @WrongUsage("/boost cancelg")
    public void giveCommand(final CommandSender sender) {

        if(sender instanceof Player){

            Player player = (Player) sender;
            cancelGlobalBoost(player,"player");

        }else{
            cancelGlobalBoost(null,"console");
        }

    }


    public void cancelPlayerBoost(Player player, Player target, String type){

        UUID uuid = target.getUniqueId();

        if(type.equals("player")){
            cache.remove(uuid);
            Message.send("general",messages
                    .getString("boost_player_cancel"),player,target,null,null,null);
            Message.send("target",messages
                    .getString("boost_player_cancel_notify"),player,target,null,null,null);
            return;
        }

        if(type.equals("console")){
            cache.remove(uuid);
            System.out.println("[Boosts] Removed personal boost for that player!");
            Message.send("target",messages
                    .getString("boost_player_cancel_notify"),player,target,null,null,null);
        }

    }

    public void cancelGlobalBoost(Player player, String type){

        if(type.equals("player")){
            gBoost.remove(1);
            Message.send("general",messages
                    .getString("gboost_cancel"),player,null,null,null,null);
            return;
        }

        if(type.equals("console")){
            gBoost.remove(1);
            System.out.println("[Boosts] Stopped global boost");
        }
    }

}
