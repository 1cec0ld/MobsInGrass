package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ZoneManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;

    public ZoneManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Zones.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }
    /*
    zones:
        kanto:
            route1:
                - Skeleton,20,list,of,time,enums
                - Skeleton,30,list,of,enums

    */
    private void initialize(){
        ConfigurationSection allGenerations = storage.getConfigurationSection("zones");

    }
}
