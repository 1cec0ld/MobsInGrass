package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlayerListener implements Listener{
    
    private MobsInGrass plugin;
    private Random r = new Random();

    public PlayerListener(MobsInGrass plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){
        UUID uuid = playerQuitEvent.getPlayer().getUniqueId();
        plugin.taskManager.removePlayer(uuid);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent){
        if (plugin.disabled){
            return;
        }
        Location from = playerMoveEvent.getFrom();
        Location to = playerMoveEvent.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()){
            return;
        }
        Material toBlock = to.getBlock().getType();
        Player player = playerMoveEvent.getPlayer();
        int rando = r.nextInt(100)+1;
        if (plugin.configManager.getPlayerChance(player,toBlock.toString()) < rando){
            return;
        }
        createRandomEntity(player,toBlock);
    }
    public void createRandomEntity(Player player, Material toBlock){
        String regionName = "default";
        Set<String> myRegions = plugin.configManager.getRegionNames(toBlock.toString());
        ApplicableRegionSet playerRegions = plugin.WorldGuard.getRegionManager(player.getWorld()).getApplicableRegions(player.getLocation());
        if (playerRegions.size() != 0){
            for (ProtectedRegion r : playerRegions){
                if (myRegions.contains(r.getId())){
                    regionName = r.getId();
                }
            }
        }
        Set<String> myMobs = plugin.configManager.getMobs(toBlock.toString(), regionName);
        if (myMobs == null){
            return;
        }
        HashMap<String,Integer> mobWeights = new HashMap<String,Integer>();
        int weightSum = 0;
        for (String o : myMobs){
            weightSum += plugin.configManager.getMobWeight(toBlock.toString(), regionName, o);
            mobWeights.put(o, plugin.configManager.getMobWeight(toBlock.toString(), regionName, o));
        }
        int iterator = 0;
        int randomIndex = r.nextInt(weightSum);
        boolean chosen = false;
        String choice = null;
        for (String o : myMobs) {
            iterator += mobWeights.get(o);
            if (!chosen && iterator >= randomIndex){
                chosen = true;
                choice = o;
            }
        }
        EntityType myEntity = EntityType.ZOMBIE;
        boolean validType = false;
        for (EntityType c : EntityType.values()){
            if (!validType && c.name().equals(choice)){
                myEntity = EntityType.valueOf(choice);
                validType = true;
            }
        }
        if (!validType){
            plugin.getServer().getConsoleSender().sendMessage("[MIG] ERROR! INVALID ENTITY TYPE IN CONFIG: "+choice);
        }
        LivingEntity spawned = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), myEntity);
        player.sendMessage(ChatColor.DARK_RED + "A WILD " + myEntity.name().replace('_', ' ') + " HAS APPEARED");
        if (plugin.configManager.hasAttributes(toBlock.toString(), regionName, myEntity.name())){
            if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "damage") > r.nextInt(100)){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "damage"));
            }
            if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "armor") > r.nextInt(100)){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_ARMOR);
                a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "armor"));
            }
            if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "knockbackresistance") > r.nextInt(100)){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "knockbackresistance"));
            }
            if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "health") > r.nextInt(100)){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "health"));
            }
            if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "movespeed") > r.nextInt(100)){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "movespeed"));
            }
            if (spawned instanceof Zombie){
                if (plugin.configManager.getAttributeChance(toBlock.toString(), regionName, myEntity.name(), "zombiereinforcement") > r.nextInt(100)){
                    AttributeInstance a = spawned.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
                    a.setBaseValue(plugin.configManager.getAttributeValue(toBlock.toString(), regionName, myEntity.name(), "zombiereinforcement"));
                }
            }
            
        }
    }
}
