package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.CustomMob;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class MobsManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    private static final List<String> booleans = Arrays.asList("angry","aware","baby","glowing","gravity","invulnerable","killer","powered","silent");
    private static final List<String> doubles = Arrays.asList("absorption","armor","armorToughness","attackDamage","attackSpeed","flyingSpeed","knockbackResist","maxHealth","movementSpeed");


    public MobsManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Mobs.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }

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
            for(String each : (section.getString("in-blocks","").split(","))){
                try{
                    Material in = Material.valueOf(each);
                    creating.addInMaterial(in);
                }catch(IllegalArgumentException ignored){}
            }
            for(String each : (section.getString("on-blocks","").split(","))){
                try{
                    Material in = Material.valueOf(each);
                    creating.addOnMaterial(in);
                }catch(IllegalArgumentException ignored){}
            }
            ConfigurationSection items = section.getConfigurationSection("items");
            if(items != null){
                setItems(creating,items);
            }
            ConfigurationSection effects = section.getConfigurationSection("potions");
            if(effects != null){
                setPotionEffects(creating,effects);
            }
            return creating;
        }catch(NullPointerException e){
            MobsInGrass.disable("No entityType found for: " + id + ", this is case sensitive \"entityType\".");
            return null;
        }
    }
    private void setItems(CustomMob mob, ConfigurationSection items){
        final List<String> valid = Arrays.asList("helmet","chestplate","leggings","boots","mainhand","offhand");
        for(String each : items.getKeys(false)){
            if(valid.contains(each)){
                ItemStack item = items.getItemStack(each);
                if(item != null){
                    mob.addItem(each,item);
                }
            } else {
                MobsInGrass.debug("Invalid equipment slot named: " + each);
            }
        }
    }
    private void setPotionEffects(CustomMob mob, ConfigurationSection potions){
        for(String each : potions.getKeys(false)){
            PotionEffectType type = PotionEffectType.getByName(potions.getString(each+".type").toUpperCase());
            if(type==null)return;
            int duration = potions.getInt(each+".duration", 999999);
            int amplifier = potions.getInt(each+".amplifier", 1);
            mob.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
        }
    }
}
