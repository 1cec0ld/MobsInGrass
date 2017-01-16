package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MobsInGrass extends JavaPlugin{
    private ConfigManager configManager;
    private TaskManager taskManager = new TaskManager(this);
    private WorldGuardPlugin WorldGuard;
    private boolean disabled = false;
    
    private WorldGuardPlugin setWorldGuard(){
        Plugin WG = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if ((WG == null) || (!(WG instanceof WorldGuardPlugin)))
        {
            this.getLogger().log(Level.SEVERE, "WorldGuard Not Found!!!!");
            return null;
        }
        this.getLogger().log(Level.INFO, "WorldGuard Plugin Loaded!");
        return (WorldGuardPlugin)WG;
    }
    
    public void onEnable(){
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

