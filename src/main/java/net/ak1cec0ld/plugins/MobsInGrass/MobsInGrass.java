package net.ak1cec0ld.plugins.MobsInGrass;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.ak1cec0ld.plugins.MobsInGrass.commands.MIGCommand;
import net.ak1cec0ld.plugins.MobsInGrass.files.FileManager;
import net.ak1cec0ld.plugins.MobsInGrass.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MobsInGrass extends JavaPlugin{
    private static MobsInGrass instance;
    private static WorldGuardPlugin WorldGuard;
    private static boolean disabled = false;
    
    public void onEnable(){
        instance = this;
        new FileManager();
        new Listeners();
        new MIGCommand();
        this.getLogger().log(Level.INFO, "MobsInGrass Loaded, v5");
        WorldGuard = setWorldGuard();
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

    public static MobsInGrass instance() {
        return instance;
    }

    public static void disable(){
        Bukkit.broadcastMessage("§4Mobs In Grass disabled.");
        disabled = true;
    }

    public static void enable(){
        Bukkit.broadcastMessage("§bMobs In Grass enabled!");
        disabled = false;
        new FileManager();
    }

    public static boolean isDisabled(){
        return disabled;
    }

    public static void debug(String string){
        Bukkit.getLogger().info("[Pokemobs-debug] " + string);
    }
}
