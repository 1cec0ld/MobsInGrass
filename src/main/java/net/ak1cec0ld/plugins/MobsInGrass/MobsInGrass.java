package net.ak1cec0ld.plugins.MobsInGrass;

import net.ak1cec0ld.plugins.MobsInGrass.commands.MIGCommand;
import net.ak1cec0ld.plugins.MobsInGrass.files.FileManager;
import net.ak1cec0ld.plugins.MobsInGrass.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MobsInGrass extends JavaPlugin{
    private static MobsInGrass instance;
    private static boolean disabled = false;
    
    public void onEnable(){
        instance = this;
        reload();
    }

    public static void reload(){
        new FileManager();
        new Listeners();
        new MIGCommand();
        instance.getLogger().info("MobsInGrass Loaded, v5");
    }

    public static MobsInGrass instance() {
        return instance;
    }

    public static void disable(String reason){
        debug(reason);
        Bukkit.broadcastMessage("§4Mobs In Grass disabled.");
        disabled = true;
    }

    public static void enable(){
        Bukkit.broadcastMessage("§bMobs In Grass enabled!");
        disabled = false;
    }

    public static boolean isDisabled(){
        return disabled;
    }

    public static void debug(String string){
        Bukkit.getLogger().info("[Pokemobs-debug] " + string);
    }
}

