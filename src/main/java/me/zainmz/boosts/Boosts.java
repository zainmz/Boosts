package me.zainmz.boosts;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import me.mattstudios.mf.base.CommandManager;
import me.zainmz.boosts.commands.Boost;
import me.zainmz.boosts.commands.Cancel;
import me.zainmz.boosts.commands.GBoost;
import me.zainmz.boosts.commands.Give;
import me.zainmz.boosts.items.ItemCreator;
import me.zainmz.boosts.listeners.*;
import me.zainmz.boosts.tasks.CacheTask;
import me.zainmz.boosts.tasks.GlobalBoostTask;
import me.zainmz.boosts.tasks.SaveCache;
import me.zainmz.boosts.utils.Message;
import me.zainmz.boosts.utils.Placeholders;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class Boosts extends JavaPlugin {

    private CommandManager commandManager;
    private final HashMap<UUID, String> cache = new HashMap<UUID, String>();
    private final HashMap<Integer, String> gBoost = new HashMap<Integer, String>();
    private final List<UUID> gBoostPlayers = new ArrayList<UUID>();
    private final HashMap<String, ItemStack> items = new HashMap<String, ItemStack>();

    private final ArrayList<String> types = new ArrayList<String>(Arrays.asList("jobspay","jobsxp", "xp", "skills"));
    private ItemCreator itemCreator;
    private BukkitTask bukkitTask;
    private BukkitTask globalTask;
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
        Set<String> keys = configuration.singleLayerKeySet("Items");
        ArrayList<String> itemNames = new ArrayList<>();
        for(String item: keys){
            itemNames.add(item.toString());
        }
        commandManager.getCompletionHandler().register("#items", input ->
                itemNames);

        //Register commands
        commandManager.register(new Boost(this));
        commandManager.register(new GBoost(this));
        commandManager.register(new Cancel(this));
        commandManager.register(new Give(this));

        //register events
        getServer().getPluginManager().registerEvents(new JobsPayEvent(this),this);
        getServer().getPluginManager().registerEvents(new XpChangeEvent(this),this);
        getServer().getPluginManager().registerEvents(new JobsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new SkillsXpGainEvent(this),this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(this),this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(this),this);
        getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
        getServer().getPluginManager().registerEvents(new CraftEvent(this),this);

        //build items from config
        this.itemCreator = new ItemCreator(this);
        itemCreator.build();

        //register PAPI placeholders
        this.placeholders = new Placeholders(this).register();

        //Run Task
        startCacheTask();
        checkGlobalBoostExists();

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
        bukkitTask = new CacheTask(this)
                .runTaskTimerAsynchronously(this,0L,20L);
    }
    public void checkGlobalBoostExists(){
        if(data.getString(String.valueOf(1)).contains(":")){
            gBoost.put(1,data.getString(String.valueOf(1)));
            data.remove(String.valueOf(1));
            globalTask = new GlobalBoostTask(this)
                    .runTaskTimerAsynchronously(this,400L,20L);
            System.out.println("[Boosts] Found a global boost, running global task!");
        }
    }

    public HashMap<UUID, String> getPlayers(){
        return cache;
    }

    //get list of players in global boost
    public List<UUID> getgBoostPlayers() {
        return gBoostPlayers;
    }

    //get global boost data
    public HashMap<Integer, String> getgBoost() {
        return gBoost;
    }

    //get items list
    public HashMap<String, ItemStack> getItems() {
        return items;
    }

    public Message getMessage() {
        return message;
    }
}
