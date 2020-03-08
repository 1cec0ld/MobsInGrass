package net.ak1cec0ld.plugins.MobsInGrass.listeners;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.CustomZone;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.ZoneProvider;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        int playerRoll = r.nextInt(100)+1;
        if (10 < playerRoll)return;                            //todo: pull 10 from the attract/repel level

        CustomZone zone = ZoneProvider.getByLocation(player.getLocation());
        if(zone == null)return;
        zone.getWeightedSpawn(TimeManager.fromServerTime(player.getWorld().getTime())).spawn(player.getLocation());
    }

   private boolean sameSpot(Location from, Location to){
        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
   }


}
