package me.zainmz.boosts;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.mattstudios.mf.base.CommandManager;
import me.zainmz.boosts.commands.Boost;
import me.zainmz.boosts.commands.Cancel;
import me.zainmz.boosts.commands.GBoost;
import me.zainmz.boosts.listeners.*;
import me.zainmz.boosts.tasks.CacheTask;
import me.zainmz.boosts.tasks.SaveCache;
import me.zainmz.boosts.utils.Message;
import me.zainmz.boosts.utils.Placeholders;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class Boosts extends JavaPlugin {

    private CommandManager commandManager;
    private final HashMap<UUID, String> cache = new HashMap<UUID, String>();
    private final HashMap<Integer, String> gBoost = new HashMap<Integer, String>();
    private final List<UUID> gBoostPlayers = new ArrayList<UUID>();

    private final ArrayList<String> types = new ArrayList<String>(Arrays.asList("jobspay","jobsxp", "xp", "skills"));
    private BukkitTask bukkitTask;
    private SaveCache saveCache;
    private Message message;
    private Boolean placeholders;

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
        commandManager.register(new Cancel(this));

        //register events
        getServer().getPluginManager().registerEvents(new JobsPayEvent(this),this);
        getServer().getPluginManager().registerEvents(new XpChangeEvent(this),this);
        getServer().getPluginManager().registerEvents(new JobsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new SkillsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this),this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this),this);

        //register PAPI placeholders
        this.placeholders = new Placeholders(this).register();

        //Run Task
        startCacheTask();

        //initialize save task
        this.saveCache = new SaveCache(this);
        //Load Data to Cache from File & Remove Data

    }


    @Override
    public void onDisable() {
        // Save Cache to File
        saveCache.save();
    }

    public void startCacheTask(){
        System.out.println("[Boosts] Beginning personal boost task");
        bukkitTask = new CacheTask(this).runTaskTimerAsynchronously(this,400L,20L);
    }

    public HashMap<UUID, String> getPlayers(){
        return cache;
    }

    //get list of players in global boost
    public List<UUID> getgBoostPlayers() {
        return gBoostPlayers;
    }

    public HashMap<Integer, String> getgBoost() {
        return gBoost;
    }

    public Message getMessage() {
        return message;
    }
}
