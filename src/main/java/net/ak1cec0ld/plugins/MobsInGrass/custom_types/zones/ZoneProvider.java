package net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.Location;

import java.util.HashMap;


public class ZoneProvider {

    private static HashMap<String, CustomZone> registeredZones = new HashMap<>();

    public static CustomZone getByRegionAndId(String region, String id){
        return registeredZones.getOrDefault((region+id).toUpperCase(), null);
    }

    public static CustomZone getByLocation(Location loc){
        for(CustomZone each : registeredZones.values()){
            if(each.inZone(loc))return each;
        }
        return null;
    }

    public static void register(String id, CustomZone zone){
        registeredZones.put(id, zone);
    }

    public static boolean hasRegistered(String name){
        return registeredZones.containsKey(name);
    }

    public static String list(){
        MobsInGrass.debug(registeredZones.keySet().toString());
        return registeredZones.keySet().toString();
    }


}
