package net.ak1cec0ld.plugins.MobsInGrass.listeners;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.MobProvider;
import org.bukkit.Location;
import org.bukkit.Material;
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
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent){
        if (MobsInGrass.isDisabled())return;
        Location to = playerMoveEvent.getTo();
        if(to == null)return;
        Location from = playerMoveEvent.getFrom();
        if (sameSpot(from,to))return;

        Material toBlock = to.getBlock().getType();
        Player player = playerMoveEvent.getPlayer();
        int playerRoll = r.nextInt(100)+1;
        if (5 < playerRoll)return;                            //todo: pull this from the attract/repel level


        MobProvider.random().spawn(player.getLocation());
    }

   private boolean sameSpot(Location from, Location to){
        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
   }


}
