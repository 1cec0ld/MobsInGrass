package net.ak1cec0ld.plugins.MobsInGrass.listeners;


import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.NamespacedKey;

public class Listeners {

    private static final NamespacedKey SPAWN_FREQUENCY_MODIFIER = new NamespacedKey(MobsInGrass.instance(),"frequency_mod");

    public Listeners(){
        new PlayerMove();
        new PlayerConsume();
        new PlayerJoin();
    }

    public static NamespacedKey getModifierTag(){
        return SPAWN_FREQUENCY_MODIFIER;
    }
}
