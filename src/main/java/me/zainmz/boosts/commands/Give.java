package me.zainmz.boosts.commands;

import de.leonhard.storage.Yaml;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.zainmz.boosts.Boosts;
import me.zainmz.boosts.items.ItemCreator;
import me.zainmz.boosts.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Command("boost")
public class Give extends CommandBase {

    private final Boosts boosts;
    private final HashMap<String, ItemStack> items;
    private ItemCreator itemCreator;

    private static final Yaml messages = Boosts.messages;

    public Give(Boosts boosts) {
        this.boosts = boosts;
        this.items = boosts.getItems();

    }

    @Permission("boosts.item.give")
    @SubCommand("giveitem")
    @Completion({"#players", "#items"})
    @WrongUsage("/boost giveitem <player>")
    public void giveItemCommand(final CommandSender sender, Player target, String item) {

        Player player = (Player) sender;

        if(target == null){
            if(sender instanceof Player){
                Message.send("general",messages
                        .getString("No Player"),player, null,null,null,null);
                return;
            }
            System.out.println("[Boosts] Player was not found!");
            return;
        }

        if(item == null){
            if(sender instanceof Player){
                Message.send("general",messages
                        .getString("No item"),player,target,null,null,null);
                return;
            }
            System.out.println("[Boosts] Item was not found!");
            return;
        }

        if(sender instanceof Player){
            Message.send("general",messages
                    .getString("given_item"),player,null,null,null,null);
        }
        System.out.println("[Boosts] Given item to that player!");

        final Map<Integer, ItemStack> map = target.getInventory().addItem(items.get(item));
        if (!map.isEmpty()) {
            target.sendMessage(ChatColor
                    .translateAlternateColorCodes('&',"&a&lBOOSTS &c Failed to give item, Your Inventory is Full!"));
        }


    }

}
