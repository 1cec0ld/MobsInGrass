package net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones;

import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import org.bukkit.Location;

import java.util.HashMap;

public class CustomZone {


    private HashMap<String,HashMap<CustomMob,Integer>> spawns;
    private String ID;
    private int x,dx,y,dy,z,dz;



    public CustomZone(String id, int x, int y, int z, int dx, int dy, int dz){
        this.ID = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        spawns = new HashMap<>();
    }


    public void addSpawn(CustomMob mob, String timeslot, int weight){

    }

    public CustomMob getSpawn(String timeslot){

        return null;
    }

    public boolean inZone(Location loc){
        return loc.getX() >= x && loc.getX() <= x+dx &&
               loc.getY() >= y && loc.getY() <= y+dy &&
               loc.getZ() >= z && loc.getZ() <= z+dz;
    }
}
