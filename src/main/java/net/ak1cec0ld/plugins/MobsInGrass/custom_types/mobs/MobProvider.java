package net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;

import java.util.HashMap;

public class MobProvider {

    private static HashMap<String,CustomMob> registeredMobs = new HashMap<>();

    public static CustomMob getById(String id){
        return registeredMobs.getOrDefault(id.toUpperCase(), null);
    }

    public static void register(String id, CustomMob mob){
        registeredMobs.put(id.toUpperCase(), mob);
    }

    public static boolean hasRegistered(String name){
        return registeredMobs.containsKey(name);
    }

    public static String list(){
        MobsInGrass.debug(registeredMobs.keySet().toString());
        return registeredMobs.keySet().toString();
    }
}
