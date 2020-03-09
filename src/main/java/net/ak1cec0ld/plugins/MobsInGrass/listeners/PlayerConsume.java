package net.ak1cec0ld.plugins.MobsInGrass.listeners;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerConsume implements Listener {


    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))return;

        if(!isAttractRepel(event.getItem()))return;

        event.getPlayer().getPersistentDataContainer().set(Listeners.getModifierTag(), PersistentDataType.DOUBLE, getModifier(event.getItem()));
    }

    private boolean isAttractRepel(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return false;


        return false;
    }
    private double getModifier(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return 1.0;
        if(!meta.hasDisplayName())return 1.0;

        String itemName = meta.getDisplayName();
        switch(itemName){
            case "§2Attract":
                return 1.2;
            case "":
                return 1.4;
            case "":
                return 1.7;
            case "":
                return 0.8;
            case "":
                return 0.6;
            case "":
                return 0.3;
            default:
                MobsInGrass.debug("Unknown displayName for Attract or Repel, talk to 1ce.");
                return 1.0;
        }

        return 1.0;
    }
}
