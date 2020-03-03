package net.ak1cec0ld.plugins.MobsInGrass;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
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
        plugin.getTaskManager().removePlayer(uuid);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent){
        if (plugin.isDisabled()){
            return;
        }
        Location from = playerMoveEvent.getFrom();
        Location to = playerMoveEvent.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()){
            return;
        }
        Material toBlock = to.getBlock().getType();
        if (!plugin.getConfigManager().isConfiguredMaterial(toBlock.toString())){
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        int rando = r.nextInt(100)+1;
        if (plugin.getConfigManager().getPlayerChance(player,toBlock.toString()) < rando){
            return;
        }
        String regionName = "default";
        Set<String> myRegions = plugin.getConfigManager().getRegionNames(toBlock.toString());
        RegionContainer getRC = WorldGuard.getInstance().getPlatform().getRegionContainer();
        ApplicableRegionSet playerRegions = getRC.createQuery().getApplicableRegions(BukkitAdapter.adapt(to));
        if (playerRegions.size() != 0){
            for (ProtectedRegion r : playerRegions){
                if (myRegions.contains(r.getId())){
                    regionName = r.getId();
                }
            }
        }
        ConfigurationSection randomEntity = plugin.getConfigManager().getRandomEntity(toBlock.toString(), regionName);
        if (randomEntity == null){
            return;
        } else {
            spawnEntity(player, randomEntity);
        }
    }
    public void spawnEntity(Player player, ConfigurationSection entitySection){
        EntityType myEntity = null;
        myEntity = EntityType.valueOf(entitySection.getName().toUpperCase());
        LivingEntity spawned = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), myEntity);
        player.sendMessage(ChatColor.DARK_RED + "A WILD " + myEntity.name().replace('_', ' ') + " HAS APPEARED");
        if (entitySection.contains("attributes")){
            if (entitySection.getDouble("attributes.damage.chance", 0.0) > r.nextDouble()*100){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                a.setBaseValue(entitySection.getInt("attributes.damage.value", 2));
            }
            if (entitySection.getDouble("attributes.armor.chance", 0.0) > r.nextDouble()*100){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_ARMOR);
                a.setBaseValue(entitySection.getInt("attributes.armor.value", 0));
            }
            if (entitySection.getDouble("attributes.knockbackresistance.chance", 0.0) > r.nextDouble()*100){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                a.setBaseValue(entitySection.getInt("attributes.knockbackresistance.value", 0));
            }
            if (entitySection.getDouble("attributes.health.chance", 0.0) > r.nextDouble()*100){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                a.setBaseValue(entitySection.getInt("attributes.health.value", 20));
            }
            if (entitySection.getDouble("attributes.movespeed.chance", 0.0) > r.nextDouble()*100){
                AttributeInstance a = spawned.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                a.setBaseValue(entitySection.getInt("attributes.movespeed.value", 1));
            }
            if (spawned instanceof Zombie){
                if (entitySection.getDouble("attributes.zombiereinforcement.chance", 0.0) > r.nextDouble()*100){
                    AttributeInstance a = spawned.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS);
                    a.setBaseValue(entitySection.getInt("attributes.zombiereinforcement.value", 0));
                }
            }
            
        }
    }
}
