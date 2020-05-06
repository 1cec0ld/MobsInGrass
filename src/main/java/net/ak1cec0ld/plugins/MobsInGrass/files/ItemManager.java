package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.ColorProvider;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.CustomItem;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.ItemProvider;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemManager {
    private static final int DEFAULT_DURATION = 60;
    private static final double DEFAULT_POWER_MULTIPLIER = 1.3;

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    public ItemManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Items.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }

    /*
      default: 8
      items:
        attract:
          displayname: &2Attract
          lore: &2An elixer that increases wild|&2encounters when drunk.
          duration: 60
          power-multiplier: 1.3
          color: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Color.html
        superattract:
          displayname: &bAttract
          lore: &bAn elixir that increases wild|&bencounters when drunk.|&bLasts longer than an Attract.
          power-multiplier: 1.3
          duration: 150
          color: AQUA
        maxattract:
          displayname: &6Max Attract
          lore: &6An elixir that increases wild|&6encounters when drunk.|&6Lasts longer than a Super Attract.
          duration: 300
          power-multiplier: 1.3
    */
    public static int getDefaultChance(){
        return storage.getInt("default", 8);
    }
    private void initialize(){
        ConfigurationSection items = storage.getConfigurationSection("items");
        for(String eachPotion : items.getKeys(false)){
            MobsInGrass.debug("Registering " + eachPotion);
            String displayName = items.getString(eachPotion+".displayname");
            if(displayName == null){
                MobsInGrass.debug("Error in collecting displayname");
                return;
            }
            displayName = displayName.replace('&', ChatColor.COLOR_CHAR);
            String lore = items.getString(eachPotion+".lore");
            if(lore == null){
                MobsInGrass.debug("Error in collecting lore");
                return;
            }
            lore = lore.replace('&',ChatColor.COLOR_CHAR);

            CustomItem item = new CustomItem(displayName, lore);
            double power = items.getDouble(eachPotion+".power-multiplier", DEFAULT_POWER_MULTIPLIER);
            item.setPower(power);
            int duration = items.getInt(eachPotion+".duration", DEFAULT_DURATION);
            item.setDuration(duration);
            String color = items.getString(eachPotion+".color");
            if(color != null){
                Color translated = ColorProvider.fromString(color);
                if(translated != null) {
                    item.setColor(translated);
                }
            }
            String mat = items.getString(eachPotion+".material");
            if(mat != null) {
                try {
                    Material material = Material.valueOf(mat.toUpperCase());
                    item.setMaterial(material);
                } catch (IllegalArgumentException ignored) {
                    MobsInGrass.debug("Material invalid");
                }
            }
            ItemProvider.register(displayName.replace(' ','_'), item);
            yml.save();
        }
    }

}
