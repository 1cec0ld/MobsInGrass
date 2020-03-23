package net.ak1cec0ld.plugins.MobsInGrass.listeners;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.CustomZone;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.ZoneProvider;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public class PlayerMove implements Listener{

    private static Random r = new Random();

    public PlayerMove(){
        MobsInGrass.instance().getServer().getPluginManager().registerEvents(this, MobsInGrass.instance());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (MobsInGrass.isDisabled())return;
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL)return;
        Location to = event.getTo();
        if(to == null)return;
        Location from = event.getFrom();
        if (sameSpot(from,to))return;

        Player player = event.getPlayer();
        int playerRoll = r.nextInt(100);
        double multiplier = getMultiplier(player);
        if (8 * multiplier < playerRoll)return;

        CustomZone zone = ZoneProvider.getByLocation(to);
        if(zone == null)return;
        CustomMob mob = zone.getWeightedSpawn(TimeManager.fromServerTime(player.getWorld().getTime()));
        if(mob == null)return;
        Material typeIn = to.getBlock().getType();
        Material typeOn = to.getBlock().getRelative(BlockFace.DOWN).getType();
        if (!mob.spawnsIn(typeIn) && !mob.spawnsOn(typeOn))return;

        Bukkit.getScheduler().runTaskLater(MobsInGrass.instance(), () -> mob.spawn(to), 20L);
    }

    private boolean sameSpot(Location from, Location to){
        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
    }
    private double getMultiplier(Player player){
        PersistentDataContainer container = player.getPersistentDataContainer();
        return container.getOrDefault(Listeners.getModifierTag(), PersistentDataType.DOUBLE, 1.0);
    }
   

}
