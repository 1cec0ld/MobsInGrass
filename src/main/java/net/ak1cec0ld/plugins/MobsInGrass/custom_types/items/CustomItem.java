package net.ak1cec0ld.plugins.MobsInGrass.custom_types.items;


import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;

public class CustomItem {
    private String id;
    private int duration;
    private double power;
    private ItemStack item;


    public CustomItem(String displayname, String lore){
        this.id = displayname.replace(' ', '_');

        item = new ItemStack(Material.POTION, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(Arrays.asList(lore.split("\\|")));

        item.setItemMeta(meta);
    }

    public void setMaterial(Material mat){
        item.setType(mat);
    }

    public ItemStack getItem(){
        return item;
    }
    public int getDuration(){
        return duration;
    }
    public double getPower(){
        return power;
    }

    public String getDisplayName(){
        return id;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    public void setPower(double power){
        this.power = power;
    }
    public void setColor(Color color){
        if(item.getItemMeta() instanceof PotionMeta){
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(color);
            item.setItemMeta(potionMeta);
        } else {
            MobsInGrass.debug("No PotionMeta Found");
        }
    }
}