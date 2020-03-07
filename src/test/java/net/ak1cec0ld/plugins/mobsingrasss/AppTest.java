package net.ak1cec0ld.plugins.mobsingrasss;

import junit.framework.TestCase;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;

public class AppTest extends TestCase{

    public static void testTimeSlotIsStart() throws Exception {
        assertEquals("evening", TimeManager.fromServerTime(11001L));
    }
    public static void testTimeSlotIsGreaterThanStart() throws Exception {
        assertEquals("day", TimeManager.fromServerTime(4500L));
    }
    public static void testTimeSlotIsGreaterThanZero() throws Exception {
        assertEquals("morning", TimeManager.fromServerTime(5L));
    }

    public static void main(String[] args) throws Exception {
        /*
        morning: 22550
        day: 4000
        evening: 11000
        night: 12000
         */
        TimeManager.addValue(22550L,"morning");
        TimeManager.addValue(4000L, "day");
        TimeManager.addValue(11000L, "evening");
        TimeManager.addValue(12000L, "night");

        testTimeSlotIsStart();
        testTimeSlotIsGreaterThanStart();
        testTimeSlotIsGreaterThanZero();
        System.out.println("All TimeManager cases passed!");
    }
}
