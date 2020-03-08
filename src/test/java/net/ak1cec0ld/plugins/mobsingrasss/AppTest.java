package net.ak1cec0ld.plugins.mobsingrasss;

import junit.framework.TestCase;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.zones.CustomZone;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.entity.EntityType;

public class AppTest extends TestCase{

    public static void testTimeSlot() throws Exception {
        TimeManager.addValue(22550L,"morning");
        TimeManager.addValue(4000L, "day");
        TimeManager.addValue(11000L, "evening");
        TimeManager.addValue(12000L, "night");
        assertEquals("day", TimeManager.fromServerTime(4500L));
        assertEquals("morning", TimeManager.fromServerTime(5L));
        assertEquals("evening", TimeManager.fromServerTime(11001L));
        assertTrue(TimeManager.contains("evening"));
    }

    public static void testZoneSpawnProvider() throws Exception{
        TimeManager.addValue(5000L,"morning");
        TimeManager.addValue(5500L,"evening");
        CustomZone zone = new CustomZone("one", new int[6]);
        zone.addSpawn(new CustomMob("mine", EntityType.BAT), new String[]{"morning", "evening"}, 40);
        assertNotNull(zone.getSpawn("morning"));
        assertNull(zone.getSpawn("foreverago"));
    }

    public static void main(String[] args) throws Exception {
        testTimeSlot();
        System.out.println("All TimeManager cases passed!");
        testZoneSpawnProvider();
        System.out.println("All Zone cases passed!");
    }
}
