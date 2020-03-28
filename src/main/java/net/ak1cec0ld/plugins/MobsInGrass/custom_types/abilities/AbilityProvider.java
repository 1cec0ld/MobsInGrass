package net.ak1cec0ld.plugins.MobsInGrass.custom_types.abilities;

import java.util.HashMap;

public class AbilityProvider {
    private static HashMap<String,Ability> registeredAbilities = new HashMap<String, Ability>();

    public static void register(Ability ability){
        registeredAbilities.put(ability.getId(), ability);
    }

    public Ability getById(String id){
        return registeredAbilities.getOrDefault(id, null);
    }
}
