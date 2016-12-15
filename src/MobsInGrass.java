package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MobsInGrass extends JavaPlugin{
    public ConfigManager configManager;
    public TaskManager taskManager = new TaskManager(this);
    public WorldGuardPlugin WorldGuard;
    public boolean disabled = false;
    
    public WorldGuardPlugin getWorldGuard(){
        Plugin WG = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if ((WG == null) || (!(WG instanceof WorldGuardPlugin)))
        {
            System.out.print("[MiG] WorldGuard Not Found!!!!");
            return null;
        }
        System.out.print("[MiG] WorldGuard Plugin Loaded!");
        return (WorldGuardPlugin)WG;
    }
    
    public void onEnable(){
        this.configManager = new ConfigManager(this);
        Server server = getServer();
        server.getPluginManager().registerEvents(new PlayerListener(this), this);
        System.out.print("[MiG] MobsInGrass Loaded, v" + configManager.getVersion());
        this.WorldGuard = getWorldGuard();

        server.getPluginCommand("attract").setExecutor(new AttractCommand(this));
        server.getPluginCommand("repel").setExecutor(new RepelCommand(this));
        server.getPluginCommand("mig").setExecutor(new MIGCommand(this));
        server.getPluginCommand("mig").setTabCompleter(new TabCompleteManager(this));
    }

    public boolean isMaterial(String input){
        for (Material m : Material.values()){
            if (input.equals(m.name())){
                return true;
            }
        }
        return false;
    }
    public boolean isEntity(String input){
        for (EntityType m : EntityType.values()){
            if (input.equals(m.name())){
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
}


//use logger for console messages