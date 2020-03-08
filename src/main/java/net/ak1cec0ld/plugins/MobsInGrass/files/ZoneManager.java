package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.CustomZone;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.ZoneProvider;
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
                x: -604
                y: 0
                z: 340
                dx: 63
                dy: 255
                dz: 127
                spawns:
                    - Skeleton,20,morning,day,evening
                    - Skeleton,30,night
    */
    private void initialize(){
        ConfigurationSection allGenerations = storage.getConfigurationSection("zones");
        for(String region : allGenerations.getKeys(false)){
            ConfigurationSection allZones = allGenerations.getConfigurationSection("region");
            for(String zone : allZones.getKeys(false)){
                String id = (region+zone).toUpperCase();
                CustomZone newZone = createCustomZone(id, allZones.getConfigurationSection(zone));
                addSpawns(newZone, allZones.getConfigurationSection(zone));



                ZoneProvider.register(id,newZone);
            }
        }
    }
    private CustomZone createCustomZone(String id, ConfigurationSection zoneSection){
        int[] intake = new int[6];
        intake[0] = zoneSection.getInt("x",0);
        intake[1] = zoneSection.getInt("y",0);
        intake[2] = zoneSection.getInt("z",0);
        intake[3] = zoneSection.getInt("dx",0);
        intake[4] = zoneSection.getInt("dy",0);
        intake[5] = zoneSection.getInt("dz",0);
        return new CustomZone(id,intake);
    }
    private void addSpawns(CustomZone zone, ConfigurationSection section){
        for(String each : section.getStringList("spawns")){
            if(!each.matches("^\\w+,\\d+(,[A-Za-z]+)+$")){
                MobsInGrass.disable("Invalid Spawn format in  " + zone.id() + ": " + each);
                return;
            }
            String[] split = each.split(",");
            CustomMob mob = MobProvider.getById(split[0]);
            if(mob == null){
                MobsInGrass.disable("Invalid CustomMob added to " + zone.id() + ": " + split[0]);
            }
            zone.addSpawn(mob, split.);
        }
    }
}
