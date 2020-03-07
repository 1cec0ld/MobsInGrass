package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.file.YamlConfiguration;

public class RegionManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;

    public RegionManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Mobs.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }

    private void initialize(){

    }
}
