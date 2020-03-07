package net.ak1cec0ld.plugins.MobsInGrass.custom_types;

import java.util.Map;
import java.util.TreeMap;

public enum TimeSlot {


    MORNING(22550,3999),
    DAY(4000,10999),
    EVENING(11000,11999),
    NIGHT(12000,22549);


    private long startTime;
    private long endTime;


    TimeSlot(long start, long end){
        this.startTime = start;
        this.endTime = end;
    }

    public static TimeSlot currentSlot(long currentTime){
        TreeMap<Long,TimeSlot> arrangedValues = makeMap();
        Map.Entry<Long, TimeSlot> e = arrangedValues.floorEntry(currentTime);
        if(e != null){
            e = arrangedValues.lowerEntry(currentTime);
        }
        return e == null ? null : e.getValue();
    }

    /* Helper function to build the TreeMap each time since I can't have static variables created during constructor */
    private static TreeMap<Long,TimeSlot> makeMap(){
        TreeMap<Long,TimeSlot> returnMe = new TreeMap<>();
        for(TimeSlot t : TimeSlot.values()){
            if(t.endTime < t.startTime){
                returnMe.put(0L,t);
            }
            returnMe.put(t.startTime, t);
        }
        return returnMe;
    }
}
