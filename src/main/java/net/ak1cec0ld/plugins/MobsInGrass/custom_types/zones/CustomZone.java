package net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.Location;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CustomZone {


    private HashMap<String, List<Pair<CustomMob,Integer>>> spawns;
    private String ID;
    private int x,dx,y,dy,z,dz;



    public CustomZone(String id, int[] bounds){
        this.ID = id;
        this.x = bounds[0];
        this.y = bounds[1];
        this.z = bounds[2];
        this.dx = bounds[3];
        this.dy = bounds[4];
        this.dz = bounds[5];
        spawns = new HashMap<>();
    }


    public void addSpawn(CustomMob mob, String[] timeslots, int weight){
        for(String eachSlot : timeslots){
            if(!TimeManager.contains(eachSlot)){
                MobsInGrass.disable("Invalid timeslot for mob " + mob.getIdentifier() + ": " + eachSlot);
                return;
            }
            List<Pair<CustomMob,Integer>> spawnsOfSlot = spawns.getOrDefault(eachSlot, new ArrayList<>());
            spawnsOfSlot.add(Pair.with(mob, weight));
            spawns.put(eachSlot,spawnsOfSlot);
        }
    }

    public String id(){
        return this.ID;
    }

    public CustomMob getSpawn(String timeslot){
        Random random = new Random();
        int totalWeights = 0;
        for(Pair<CustomMob,Integer> each : spawns.getOrDefault(timeslot, new ArrayList<>())){
            totalWeights += each.getValue1();
        }
        if(totalWeights <= 0) return null;
        int index = random.nextInt(totalWeights);
        for(Pair<CustomMob,Integer> each : spawns.getOrDefault(timeslot, new ArrayList<>())){
            totalWeights -= each.getValue1();
            if(index > totalWeights)return each.getValue0();
        }
        return null;
    }

    public boolean inZone(Location loc){
        return loc.getX() >= x && loc.getX() <= x+dx &&
               loc.getY() >= y && loc.getY() <= y+dy &&
               loc.getZ() >= z && loc.getZ() <= z+dz;
    }
}
