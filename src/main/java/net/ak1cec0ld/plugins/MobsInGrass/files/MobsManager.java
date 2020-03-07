package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class MobsManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    private static final List<String> booleans = Arrays.asList("aware","baby","glowing","gravity","invulnerable","killer","powered","silent");
    private static final List<String> doubles = Arrays.asList("absorption","armor","armorToughness","attackDamage","attackSpeed","flyingSpeed","knockbackResist","maxHealth","movementSpeed");


    public MobsManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Mobs.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }
    /*
    mobs:
        ID:
            entityType: {https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Mob.html}
        ##    amount: {int}
        ##    size: {int}               slime/magmacube only
        ##    absorption: {double}
        ##    armor: {double}
        ##    armorToughness: {double}
        ##    attackDamage: {double}    max 2048
        ##    attackSpeed: {double}
        ##    flyingSpeed: {double}
        ##    knockbackResist: {double}
        ##    maxHealth: {double}       max 1024
        ##    movementSpeed: {double}
        ##    aware: {bool}
        ##    baby: {bool}
        ##    glowing: {bool}
        ##    gravity: {bool}
        ##    invulnerable: {bool}
        ##    killer: {bool}            rabbits only
        ##    powered: {bool}           creepers only
        ##    silent: {bool}
        ##    announcement: {&4string}
        ##    tags: {string1,string2,string3,string4} Note, these will eventually determine mob powers
        ##    passengers:
        ##        - ID1
        ##        - ID2...etc
        ##        - Note, these must be defined ABOVE this point!
     */

    private void initialize(){
        ConfigurationSection allMobs = storage.getConfigurationSection("mobs");
        for(String eachId : allMobs.getKeys(false)){
            CustomMob mob = createCustomMob(eachId, allMobs.getConfigurationSection(eachId));
            if(mob==null)return;
            MobProvider.register(eachId,mob);
        }
    }

    private CustomMob createCustomMob(String id, ConfigurationSection section){
        try {
            CustomMob creating = new CustomMob(id, EntityType.valueOf(section.getString("entityType").toUpperCase()));
            creating.setAmount(section.getInt("amount", 1));
            creating.setSize(section.getInt("size",1));
            for(String eachBool : booleans){
                if(section.contains(eachBool)){
                    creating.setBool(eachBool, section.getBoolean(eachBool));
                }
            }
            for(String eachDouble : doubles){
                if(section.contains(eachDouble)){
                    creating.setDouble(eachDouble, section.getDouble(eachDouble));
                }
            }
            List passengers = section.getList("passengers");
            if(passengers != null) {
                for (Object passengerId : passengers) {
                    creating.addPassenger(MobProvider.getById((String) passengerId));
                }
            }
            creating.setTags(section.getString("tags", null));
            creating.setAnnouncement(section.getString("announcement",null));
            return creating;
        }catch(NullPointerException e){
            MobsInGrass.debug("No entityType found for: " + id + ", this is case sensitive \"entityType\".");
            MobsInGrass.disable();
            return null;
        }
    }

    public static void reload(){
        yml.reload();
        storage = yml.getYamlConfiguration();
    }
}
