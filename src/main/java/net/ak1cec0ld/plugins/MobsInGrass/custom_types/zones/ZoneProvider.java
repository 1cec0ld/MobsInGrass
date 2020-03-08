package net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;

import java.util.HashMap;


public class ZoneProvider {

    private static HashMap<String, CustomZone> registeredZones = new HashMap<>();

    public static CustomZone getById(String id){
        return registeredZones.getOrDefault(id.toUpperCase(), null);
    }

    public static void register(String id, CustomZone zone){
        registeredZones.put(id.toUpperCase(), zone);
    }

    public static boolean hasRegistered(String name){
        return registeredZones.containsKey(name);
    }

    public static String list(){
        MobsInGrass.debug(registeredZones.keySet().toString());
        return registeredZones.keySet().toString();
    }


}
