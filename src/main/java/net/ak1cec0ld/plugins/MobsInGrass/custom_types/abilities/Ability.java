package net.ak1cec0ld.plugins.MobsInGrass.custom_types.abilities;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public abstract class Ability {
    protected String id;
    protected Entity entity;

    protected void register(){
        AbilityProvider.register(this);
    }

    public String getId(){
        return id;
    }
    public void applyToEntity(){
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(new NamespacedKey(MobsInGrass.instance(),id), PersistentDataType.STRING, id);
    }
    public abstract void execute();
}
