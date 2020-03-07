package net.ak1cec0ld.plugins.mobsingrasss;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.TimeSlot;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase{

    public static void testTimeSlotCurrentSlot() throws Exception {
        assertEquals( TimeSlot.MORNING, TimeSlot.currentSlot(5L) );
    }
    public static void testTimeSlotCurrentSlot2() throws Exception {
        assertEquals( TimeSlot.EVENING, TimeSlot.currentSlot(11500L) );
    }

    public static void main(String[] args) throws Exception {
        testTimeSlotCurrentSlot();
        testTimeSlotCurrentSlot2();
    }
}
