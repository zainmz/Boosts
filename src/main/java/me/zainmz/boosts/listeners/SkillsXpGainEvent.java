package me.zainmz.boosts.listeners;

import com.archyx.aureliumskills.api.XpGainEvent;
import me.zainmz.boosts.Boosts;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

public class SkillsXpGainEvent implements Listener {

    private final Boosts boosts;
    private final HashMap<UUID, String> cache;

    public SkillsXpGainEvent(Boosts boosts) {
        this.boosts = boosts;
        this.cache = boosts.getPlayers();
    }

    @EventHandler
    public void onSkillsXpGain(XpGainEvent event){

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if(cache.containsKey(uuid)){
            if(cache.get(uuid).contains("skills")){
                String[] data = cache.get(uuid).split(":");
                double multi = Double.parseDouble(data[1]);
                double xp = event.getAmount();

                //getting new payment
                double newXp = xp + (xp * multi);
                event.setAmount(newXp);

            }
        }
    }


}
