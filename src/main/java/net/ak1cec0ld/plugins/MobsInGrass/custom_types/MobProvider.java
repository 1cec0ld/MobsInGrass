package net.ak1cec0ld.plugins.MobsInGrass.custom_types;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;

import java.util.HashMap;
import java.util.Random;

public class MobProvider {

    private static HashMap<String,CustomMob> registeredMobs = new HashMap<>();

    public static CustomMob getById(String id){
        return registeredMobs.getOrDefault(id.toUpperCase(), null);
    }

    public static void register(String id, CustomMob mob){
        registeredMobs.put(id.toUpperCase(), mob);
    }

    public static String listMobs(){
        MobsInGrass.debug(registeredMobs.keySet().toString());
        return registeredMobs.keySet().toString();
    }

    public static CustomMob random(){
        Random r = new Random();
        String x = (String) registeredMobs.keySet().toArray()[r.nextInt(registeredMobs.size())];
        return registeredMobs.get(x);
    }


}
