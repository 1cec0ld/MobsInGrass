package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.ConfigurationSection;
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
    /*
    regions:
        kanto:
            route1:
                - Skeleton,20,list,of,time,enums
                - Skeleton,30,list,of,enums

    */
    private void initialize(){
        ConfigurationSection allGenerations = storage.getConfigurationSection("regions");

    }
}
