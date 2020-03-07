package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.TreeMap;

public class TimeManager {



    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;

    private static TreeMap<Long, String> mappedValues = new TreeMap<>();

    public TimeManager(){

        yml = new CustomYMLStorage(MobsInGrass.instance(),"TimeSlots.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }

    private void initialize(){
        mappedValues.clear();
        ConfigurationSection times = storage.getConfigurationSection("times");
        for(String eachTimeSlot : times.getKeys(false)){
            addValue(times.getLong(eachTimeSlot), eachTimeSlot);
        }
    }

    public static void addValue(Long startTime, String timeSlot){
        mappedValues.put(startTime, timeSlot);
    }

    public static String fromServerTime(long currentTime){
        Map.Entry<Long, String> e = mappedValues.floorEntry(currentTime);
        if(e != null){
            e = mappedValues.lowerEntry(currentTime);
        }
        return e == null ? mappedValues.lastEntry().getValue() : e.getValue();
    }
}
