package me.zainmz.boosts;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.mattstudios.mf.base.CommandManager;
import me.zainmz.boosts.commands.Boost;
import me.zainmz.boosts.listeners.JobsPayEvent;
import me.zainmz.boosts.listeners.JobsXpGainEvent;
import me.zainmz.boosts.listeners.XpChangeEvent;
import me.zainmz.boosts.tasks.CacheTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class Boosts extends JavaPlugin {

    private CommandManager commandManager;
    private final HashMap<String, String> cache = new HashMap<String, String>();
    private final ArrayList<String> types = new ArrayList<String>(Arrays.asList("jobspay","jobsxp", "xp", "skills"));
    private BukkitTask bukkitTask;

    public static Yaml configuration;
    public static Yaml messages;
    public static Yaml data;


    @Override
    public void onEnable() {

        //initialize configurations
        configuration = LightningBuilder
                .fromPath("Config", "plugins/Boosts")
                .addInputStreamFromResource("Config.yml")
                .createYaml();

        messages = LightningBuilder
                .fromPath("Messages", "plugins/Boosts")
                .addInputStreamFromResource("Messages.yml")
                .createYaml();

        data = LightningBuilder
                .fromPath("Data", "plugins/Boosts")
                .addInputStreamFromResource("Data.yml")
                .createYaml();


        //initialize command manager
        this.commandManager = new CommandManager(this);


        //Register Tab Completion
        commandManager.getCompletionHandler().register("#types", input ->
                types);

        //Register commands
        commandManager.register(new Boost(this));

        //register events
        getServer().getPluginManager().registerEvents(new JobsPayEvent(this),this);
        getServer().getPluginManager().registerEvents(new XpChangeEvent(this),this);
        getServer().getPluginManager().registerEvents(new JobsXpGainEvent(this),this);
        //Run Task
        System.out.println("[Boosts] Beginning task in 20s..");
        bukkitTask = new CacheTask(this).runTaskTimerAsynchronously(this,400L,20L);
        //Load Data to Cache from File & Remove Data

    }


    @Override
    public void onDisable() {
        // Save Cache to File
    }

    public HashMap<String, String> getPlayers(){
        return cache;
    }
}
