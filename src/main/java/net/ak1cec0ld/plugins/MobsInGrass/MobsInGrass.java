package net.ak1cec0ld.plugins.MobsInGrass;

import java.util.logging.Level;

import net.ak1cec0ld.plugins.MobsInGrass.commands.AttractCommand;
import net.ak1cec0ld.plugins.MobsInGrass.commands.MIGCommand;
import net.ak1cec0ld.plugins.MobsInGrass.commands.RepelCommand;
import net.ak1cec0ld.plugins.MobsInGrass.commands.TabCompleteManager;
import net.ak1cec0ld.plugins.MobsInGrass.files.ConfigManager;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MobsInGrass extends JavaPlugin{
    private static MobsInGrass instance;
    private ConfigManager configManager;
    private TaskManager taskManager = new TaskManager(this);
    private WorldGuardPlugin WorldGuard;
    private boolean disabled = false;
    
    public void onEnable(){
        instance = this;
        this.configManager = new ConfigManager(this);
        Server server = getServer();
        server.getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getLogger().log(Level.INFO, "MobsInGrass Loaded, v" + configManager.getVersion());
        this.WorldGuard = setWorldGuard();

        server.getPluginCommand("attract").setExecutor(new AttractCommand(this));
        server.getPluginCommand("repel").setExecutor(new RepelCommand(this));
        server.getPluginCommand("mig").setExecutor(new MIGCommand(this));
        server.getPluginCommand("mig").setTabCompleter(new TabCompleteManager(this));
    }

    private static WorldGuardPlugin setWorldGuard(){
        Plugin WG = instance.getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(WG instanceof WorldGuardPlugin))
        {
            instance.getLogger().severe("WorldGuard Not Found!!!!");
            return null;
        }
        instance.getLogger().info("WorldGuard Plugin Loaded!");
        return (WorldGuardPlugin)WG;
    }

    public void disable(){
        this.disabled = true;
    }
    public void enable(){
        this.disabled = false;
    }
    public boolean isDisabled(){
        return this.disabled;
    }

    public boolean isEntity(String input){
        for (EntityType m : EntityType.values()){
            if (input.toUpperCase().equals(m.name())){
                return true;
            }
        }
        return false;
    }
    public boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    public boolean isDouble(String input){
        try{
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
    public ConfigManager getConfigManager(){
        return this.configManager;
    }
    public TaskManager getTaskManager(){
        return this.taskManager;
    }
    public WorldGuardPlugin getWorldGuard(){
        return this.WorldGuard;
    }
}

