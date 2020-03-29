package net.ak1cec0ld.plugins.MobsInGrass.listeners.player;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.CustomItem;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.ItemProvider;
import net.ak1cec0ld.plugins.MobsInGrass.listeners.Listeners;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerConsume implements Listener {

    public PlayerConsume(){
        MobsInGrass.instance().getServer().getPluginManager().registerEvents(this, MobsInGrass.instance());
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))return;

        CustomItem item = ItemProvider.getByItemStack(event.getItem());
        if(item == null)return;

        PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
        container.set(Listeners.getSpawnModifierKey(), PersistentDataType.DOUBLE, getModifier(event.getItem()));

        MobsInGrass.instance().getServer().getScheduler().runTaskLater(MobsInGrass.instance(), () -> container.remove(Listeners.getSpawnModifierKey()), getDuration(event.getItem())*20);
    }

    private double getModifier(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return 1.0;
        if(!meta.hasDisplayName())return 1.0;

        String itemName = meta.getDisplayName();
        CustomItem customItem = ItemProvider.getByDisplayName(itemName);
        if(customItem == null)return 1.0;

        return customItem.getPower();
    }

    private int getDuration(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return 0;
        if(!meta.hasDisplayName())return 0;

        String itemName = meta.getDisplayName();
        CustomItem customItem = ItemProvider.getByDisplayName(itemName);
        if(customItem == null)return 0;

        return customItem.getDuration();
    }

}
