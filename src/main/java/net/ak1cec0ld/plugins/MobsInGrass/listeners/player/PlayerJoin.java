package net.ak1cec0ld.plugins.MobsInGrass.listeners.player;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.listeners.Listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    public PlayerJoin(){
        MobsInGrass.instance().getServer().getPluginManager().registerEvents(this, MobsInGrass.instance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getPersistentDataContainer().remove(Listeners.getSpawnModifierKey());
    }

}
