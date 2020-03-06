package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    public ItemManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Items.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();
    }
}
