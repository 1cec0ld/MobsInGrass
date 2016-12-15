package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager {
    
    private MobsInGrass plugin;
    private HashMap<UUID,BukkitTask> tasks = new HashMap<UUID,BukkitTask>();
    private HashSet<UUID> attractors = new HashSet<UUID>();
    private HashSet<UUID> repellors = new HashSet<UUID>();

    public TaskManager(MobsInGrass plugin){
        this.plugin = plugin;
    }

    public void attract(Player player, Long duration) {
        final UUID uuid = player.getUniqueId();
        if (tasks.containsKey(uuid)){
            tasks.get(uuid).cancel();
        }
        attractors.add(uuid);
        repellors.remove(uuid);
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskLater(plugin, new Runnable(){
            public void run(){
                attractors.remove(uuid);
                tasks.remove(uuid);
                player.sendMessage(ChatColor.DARK_RED + "You feel the effects of the attract wear off...");
            }
        }, duration);
        tasks.put(uuid, task);
    }
    public boolean isAttracting(Player player){
        return attractors.contains(player.getUniqueId());
    }
    public void repel(Player player, Long duration) {
        final UUID uuid = player.getUniqueId();
        if (tasks.containsKey(uuid)){
            tasks.get(uuid).cancel();
        }
        repellors.add(uuid);
        attractors.remove(uuid);
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        BukkitTask task = scheduler.runTaskLater(plugin, new Runnable(){
            public void run(){
                repellors.remove(uuid);
                tasks.remove(uuid);
                player.sendMessage(ChatColor.DARK_RED + "You feel the effects of the repel wear off...");
            }
        }, duration);
        tasks.put(uuid, task);
    }
    public boolean isRepelling(Player player){
        return repellors.contains(player.getUniqueId());
    }
    public void removePlayer(UUID uuid){
        if (tasks.containsKey(uuid)){
            tasks.get(uuid).cancel();
        }
        tasks.remove(uuid);
        attractors.remove(uuid);
        repellors.remove(uuid);
    }
}
