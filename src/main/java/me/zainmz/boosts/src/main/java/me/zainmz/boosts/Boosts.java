package me.zainmz.boosts;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.mattstudios.mf.base.CommandManager;
import me.zainmz.boosts.commands.Boost;
import me.zainmz.boosts.commands.GBoost;
import me.zainmz.boosts.listeners.*;
import me.zainmz.boosts.tasks.CacheTask;
import me.zainmz.boosts.tasks.GlobalBoostTask;
import me.zainmz.boosts.tasks.SaveCache;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class Boosts extends JavaPlugin {

    private CommandManager commandManager;
    private final HashMap<UUID, String> cache = new HashMap<UUID, String>();
    private String gBoost = null;
    private final List<UUID> gBoostPlayers = new ArrayList<UUID>();

    private final ArrayList<String> types = new ArrayList<String>(Arrays.asList("jobspay","jobsxp", "xp", "skills"));
    private BukkitTask bukkitTask;
    private BukkitTask globalTask;
    private SaveCache saveCache;

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
        commandManager.register(new GBoost(this));

        //register events
        getServer().getPluginManager().registerEvents(new JobsPayEvent(this),this);
        getServer().getPluginManager().registerEvents(new XpChangeEvent(this),this);
        getServer().getPluginManager().registerEvents(new JobsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new SkillsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this),this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this),this);

        //Run Task
        System.out.println("[Boosts] Beginning task");
        bukkitTask = new CacheTask(this).runTaskTimerAsynchronously(this,400L,20L);
        globalTask = new GlobalBoostTask(this).runTaskTimerAsynchronously(this,400L,20L);

        //initialize save task
        this.saveCache = new SaveCache(this);
        //Load Data to Cache from File & Remove Data

    }


    @Override
    public void onDisable() {
        // Save Cache to File
        saveCache.save();
    }

    public HashMap<UUID, String> getPlayers(){
        return cache;
    }

    //get list of players in global boost
    public List<UUID> getgBoostPlayers() {
        return gBoostPlayers;
    }

    //get global boost type and data
    public String getgBoost() {
        return gBoost;
    }
}
