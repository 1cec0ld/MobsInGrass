package net.ak1cec0ld.plugins.MobsInGrass.files;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {
    
    private FileConfiguration config;
    private MobsInGrass plugin;
    private HashMap<String,String> materialCache = new HashMap<>();
    private Random r = new Random();

    
    public ConfigManager(MobsInGrass plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
        
        if(validateConfig()){
            plugin.getLogger().info("[MiG] Configuration Loaded Successfully!");
            plugin.enable();
            fillMaterialCache();
        } else {
            plugin.getLogger().severe("[MiG] Configuration Loaded With Above Errors!");
            plugin.disable();
        }
    }
    
    public void loadConfig(FileConfiguration file){
        config = file;
        fillMaterialCache();
        plugin.getLogger().info( "Configuration loaded");
    }
    
    public String getVersion(){
        return config.getString("version");
    }
    
    public Integer getAttractCount(){
        return config.getInt("settings.global.attract.amount", 10);
    }
    public String getAttractMaterial(){
        return config.getString("settings.global.attract.itemtype", "GLOWSTONE_DUST");
    }
    public Long getAttractDuration(){
        return new Long(config.getInt("settings.global.attract.duration", 15))*20;
    }
    
    public Integer getRepelCount(){
        return config.getInt("settings.global.repel.amount", 10);
    }
    public String getRepelMaterial(){
        return config.getString("settings.global.repel.itemtype", "REDSTONE");
    }
    public Long getRepelDuration(){
        return new Long(config.getInt("settings.global.repel.duration", 15)*20);
    }

    public Double getPlayerChance(Player player, String toBlock){
        String path;
        if (plugin.getTaskManager().isAttracting(player)){
            path = materialCache.get(toBlock.toUpperCase())+".chances.attract";
        } else if (plugin.getTaskManager().isRepelling(player)){
            path = materialCache.get(toBlock.toUpperCase())+".chances.repel";
        } else {
            path = materialCache.get(toBlock.toUpperCase())+".chances.base";
        }
        return config.getDouble(path, 0.0);
    }
    public ConfigurationSection getRandomEntity(String toBlock, String regionName){
        int weightSum = 0;
        if (config.contains(materialCache.get(toBlock.toUpperCase())+".regions."+regionName)){
            for (String mobName : config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions."+regionName).getKeys(false)){
                if (plugin.isEntity(mobName)){
                    weightSum += config.getInt(materialCache.get(toBlock.toUpperCase())+".regions."+regionName+"."+mobName+".weight", 0);
                }
            }
            int iterator = 0;
            int randomIndex = r.nextInt(weightSum);
            for (String mobName : config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions."+regionName).getKeys(false)){
                iterator += config.getInt(materialCache.get(toBlock.toUpperCase())+".regions."+regionName+"."+mobName+".weight");
                if (iterator >= randomIndex){
                    return config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions."+regionName+"."+mobName);
                }
            }
        }
        return null;
    }
    public Set<String> getRegionNames(String toBlock){
        return config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions").getKeys(false);
    }
    public Set<String> getMaterialsForThisMob(String mobName){
        Set<String> compilation = new HashSet<String>();
        for(String blockType : config.getConfigurationSection("settings.blocks").getKeys(false)){
            for(String regionName : config.getConfigurationSection("settings.blocks."+blockType+".regions").getKeys(false)){
                if (config.getConfigurationSection("settings.blocks."+blockType+".regions."+regionName).contains(mobName)){
                    compilation.add(blockType);
                }  
            }
        }
        if (compilation.isEmpty()){
            compilation.add(ChatColor.RED+"No Blocks can spawn "+mobName+"!");
        }
        return compilation;
    }
    public Set<String> getMobsForThisMaterial(String toBlock){
        Set<String> compilation = new HashSet<String>();
        if (materialCache.containsKey(toBlock.toUpperCase())){
            for (String regionName : config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions").getKeys(false)){
                for (String mobName : config.getConfigurationSection(materialCache.get(toBlock.toUpperCase())+".regions."+regionName).getKeys(false)){
                    if (!compilation.contains(mobName)){
                        compilation.add(mobName);
                    }
                }
            }
        }
        if (compilation.isEmpty()){
            compilation.add(ChatColor.RED+"No Mobs can spawn in "+toBlock+"!");
        }
        return compilation;
    }
    
    public Collection<String> getConfiguredMaterials(){
        /*Set<String> compilation = new HashSet<String>();
        if (config.contains("settings.blocks")){
            for (String blockName : config.getConfigurationSection("settings.blocks").getKeys(false)){
                if (config.getConfigurationSection("settings.blocks."+blockName+".regions").getKeys(false).size() > 0){
                    compilation.add(blockName);
                }
            }
            return compilation;
        }
        return null;*/
        return materialCache.keySet();
    }
    public boolean isConfiguredMaterial(String materialName){
        return materialCache.containsKey(materialName.toUpperCase());
    }
    
    public boolean validateConfig(){
        boolean valid = true;
        if (config.contains("settings.blocks")){
            for (String section : config.getConfigurationSection("settings.blocks").getKeys(false)){
                if (!validateBlock(config.getConfigurationSection("settings.blocks."+section))){
                    valid = false;
                }
            }
        } else {
            plugin.getLogger().log(Level.SEVERE, "Config is missing any blocks!");
            valid = false;
        }
        return valid;
    }
    private boolean validateBlock(ConfigurationSection input){
        boolean valid = true;
        if (Material.matchMaterial(input.getName()) == null){
            plugin.getLogger().log(Level.SEVERE, "Config contains a block that isn't a valid material: "+input.getName());
            valid = false;
        } else {
            if (!input.contains("regions")){
                plugin.getLogger().log(Level.SEVERE, "Config contains a block without any regions: "+input.getName());
                valid = false;
            } else {
                for (String section : input.getConfigurationSection("regions").getKeys(false)){
                    if (!validateRegion(input.getConfigurationSection("regions."+section))){
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }
    private boolean validateRegion(ConfigurationSection input){
        boolean valid = true;
        for (String mobName : input.getKeys(false)){
            if (!plugin.isEntity(mobName)){
                plugin.getLogger().log(Level.SEVERE, "Config contains a block with an invalid mob name: "+input.getName()+"->"+mobName);
                valid = false;
            } else {
                if (!validateMob(input.getConfigurationSection(mobName))){
                    valid = false;
                }
            }
        }
        return valid;
    }
    private boolean validateMob(ConfigurationSection input){
        boolean valid = true;
        if (!input.contains("weight")){
            plugin.getLogger().log(Level.SEVERE, "Config contains a block without any regions: "+input.getName());
            valid = false;
        } else {
            if (input.contains("attributes")){
                String validAttributes = "damage,health,movespeed,armor,knockbackresistance,zombiereinforcement";
                for (String attName : input.getConfigurationSection("attributes").getKeys(false)){
                    if (!StringUtils.containsIgnoreCase(validAttributes,attName)){
                        valid = false;
                        plugin.getLogger().log(Level.SEVERE, "Config Has a mob with an invalid attribute name: "+input.getName()+"'s "+attName);
                        plugin.getLogger().log(Level.SEVERE, "Valid attributes are lowercase: damage, health, movespeed, armor, knockbackresistance, zombiereinforcement");
                    } else {
                        if (!input.contains("attributes."+attName+".chance")){
                            valid = false;
                            plugin.getLogger().log(Level.SEVERE, "Config Has a mob with an attribute without a 'chance' to happen: "+input.getName()+"'s "+attName);
                        }
                        if (!input.contains("attributes."+attName+".value")){
                            valid = false;
                            plugin.getLogger().log(Level.SEVERE, "Config Has a mob with an attribute without a 'value' to it: "+input.getName()+"'s "+attName);
                        }
                    }
                }
                if (!input.getName().equalsIgnoreCase("ZOMBIE")){
                    if (input.contains("attributes.zombiereinforcement")){
                        valid = false;
                        plugin.getLogger().log(Level.SEVERE, "Config Has a non-Zombie with Zombiereinforcement attribute: "+input.getName());
                    }
                }
            }
        }
        return valid;
    }
    
    private void fillMaterialCache(){
        materialCache.clear();
        for (String blockName : config.getConfigurationSection("settings.blocks").getKeys(false)){
            if (Material.matchMaterial(blockName) != null){
                materialCache.put(blockName.toUpperCase(), "settings.blocks."+blockName);
                plugin.getLogger().log(Level.SEVERE, "Added block to materialCache: "+blockName);
            } else {
                plugin.getLogger().log(Level.SEVERE, "Mislabeled Block in config: "+blockName);
            }
        }
    }
    
    public void setMaterialChance(String blockName, String type, Double value){
        config.set(materialCache.get(blockName.toUpperCase())+".chances."+type, value);
        plugin.saveConfig();
    }
    public boolean removeEntityFromMaterial(String blockName, String mobName, String regionName){
        if (config.contains(materialCache.get(blockName.toUpperCase())+".regions."+regionName+"."+mobName)){
            config.getConfigurationSection(materialCache.get(blockName.toUpperCase())+".regions."+regionName).set(mobName, null);
            if (config.getConfigurationSection(materialCache.get(blockName.toUpperCase())+".regions."+regionName).getKeys(false).size() == 0){
                removeRegionFromMaterial(blockName, regionName);
            }
            try {
                config.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    private void removeRegionFromMaterial(String blockName, String regionName){
        if (config.contains(materialCache.get(blockName.toUpperCase())+".regions."+regionName)){
            config.set(materialCache.get(blockName.toUpperCase())+".regions."+regionName, null);
        }
        if (config.getConfigurationSection(materialCache.get(blockName.toUpperCase())+".regions").getKeys(false).size() == 0){
            removeMaterialFromConfig(blockName);
        }
    }
    private void removeMaterialFromConfig(String blockName){
        if (config.contains(materialCache.get(blockName.toUpperCase()))){
            config.set(materialCache.get(blockName.toUpperCase()), null);
            materialCache.remove(blockName.toUpperCase());
        }
    }
    public boolean addEntityToMaterial(String blockName, String mobName, String regionName, Integer weight){
        if (config.contains(materialCache.get(blockName.toUpperCase())+".regions."+regionName+"."+mobName+".weight")){
            if (config.getInt(materialCache.get(blockName.toUpperCase())+".regions."+regionName+"."+mobName+".weight") == weight){
                return false;
            } else {
                config.set(materialCache.get(blockName.toUpperCase())+".regions."+regionName+"."+mobName+".weight", weight);
                try {
                    config.save(new File(plugin.getDataFolder(), "config.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        } else if (materialCache.containsKey(blockName.toUpperCase())){
            config.set(materialCache.get(blockName.toUpperCase())+".regions."+regionName+"."+mobName+".weight", weight);
            try {
                config.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fillMaterialCache();
            return true;
        } else {
            config.set("settings.blocks."+blockName+".regions."+regionName+"."+mobName+".weight", weight);
            try {
                config.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            fillMaterialCache();
            return true;
        }
    }
}