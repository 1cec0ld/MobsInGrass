package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class ConfigManager {
    
    private FileConfiguration config;
    private MobsInGrass plugin;
    
    public ConfigManager(MobsInGrass plugin){
        this.config = plugin.getConfig();
        this.plugin = plugin;

        config.options().copyDefaults(true);
        plugin.saveConfig();
        
        if(validateConfig()){
            plugin.getServer().getConsoleSender().sendMessage("[MiG] Configuration Loaded Successfully!");
        } else {
            plugin.getServer().getConsoleSender().sendMessage("[MiG] Configuration Loaded With Above Errors!");
        }
    }
    
    public void setConfig(FileConfiguration file){
        plugin.getServer().getConsoleSender().sendMessage("Set Config");
        config = file;
    }
    public boolean validateConfig(){
        boolean valid = true;
        if (!config.contains("settings")){
            valid = false;
            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No 'settings' at all!");
        } else {
            if (!config.contains("settings.global")){
                valid = false;
                plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No 'global' Settings!");
            } else {
                if (!config.contains("settings.global.attract")){
                    valid = false;
                    plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No 'attract' Settings!");
                } else {
                    if (!config.contains("settings.global.attract.itemtype")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Attract 'itemtype' Set!");
                    }
                    if (!config.contains("settings.global.attract.amount")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Attract 'amount' Set!");
                    }
                    if (!config.contains("settings.global.attract.duration")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Attract 'duration' Set!");
                    }
                }
                if (!config.contains("settings.global.repel")){
                    valid = false;
                    plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No 'repel' Settings!");
                } else {
                    if (!config.contains("settings.global.repel.itemtype")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Repel 'itemtype' Set!");
                    }
                    if (!config.contains("settings.global.repel.amount")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Repel 'amount' Set!");
                    }
                    if (!config.contains("settings.global.repel.duration")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has No Repel 'duration' Set!");
                    }
                }
            }
            if (!config.contains("settings.blocks")){
                valid = false;
                plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has Nothing Set for 'blocks' triggers!");
            } else {
                //Ensure every blocktype listed is a valid Material, caps included
                for (String blockName : config.getConfigurationSection("settings.blocks").getKeys(false)){
                    if (!plugin.isMaterial(blockName)){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has an improperly named or not CapsLocked Material: '"+blockName+"'");
                    }
                    if (!config.contains("settings.blocks."+blockName+".chances")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a Material without any 'chances' to trigger mobs: "+blockName);
                    } else {
                        if (!config.contains("settings.blocks."+blockName+".chances.base")){
                            valid = false;
                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a Material without a 'base' chance to trigger mobs: "+blockName);
                        }
                        if (!config.contains("settings.blocks."+blockName+".chances.attract")){
                            plugin.getServer().getConsoleSender().sendMessage("[MiG] [Warning] Config Has a Material without an 'attract' chance modifier to trigger mobs: "+blockName);
                        }
                        if (!config.contains("settings.blocks."+blockName+".chances.repel")){
                            plugin.getServer().getConsoleSender().sendMessage("[MiG] [Warning] Config Has a Material without a 'repel' chance modifier to trigger mobs: "+blockName);
                        }
                    }
                    if (!config.contains("settings.blocks."+blockName+".regions")){
                        valid = false;
                        plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a Material without any 'regions' where mobs can spawn: "+blockName);
                    } else {
                        for (String rName : config.getConfigurationSection("settings.blocks."+blockName+".regions").getKeys(false)){
                            for (String mobName : config.getConfigurationSection("settings.blocks."+blockName+".regions."+rName).getKeys(false)){
                                if (!plugin.isEntity(mobName)){
                                    valid = false;
                                    plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has an improperly named or not CapsLocked MobName: '"+mobName+"'");
                                }
                                if (!config.contains("settings.blocks."+blockName+".regions."+rName+"."+mobName+".weight")){
                                    valid = false;
                                    plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a mob without a weight (chance to spawn): "+blockName+ "->" + rName+ "->" + mobName);
                                }
                                if (config.contains("settings.blocks."+blockName+".regions."+rName+"."+mobName+".attributes")){
                                    String validAttributes = "damage,health,movespeed,armor,knockbackresistance,zombiereinforcement";
                                    for (String attName : config.getConfigurationSection("settings.blocks."+blockName+".regions."+rName+"."+mobName+".attributes").getKeys(false)){
                                        if (!validAttributes.contains(attName)){
                                            valid = false;
                                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a mob with an invalid attribute name: "+mobName+"'s "+attName);
                                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Valid attributes are lowercase: damage, health, movespeed, armor, knockbackresistance, zombiereinforcement");
                                        }
                                        if (!config.contains("settings.blocks."+blockName+".regions."+rName+"."+mobName+".attributes."+attName+".chance")){
                                            valid = false;
                                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a mob with an attribute without a 'chance' to happen: "+mobName+"'s "+attName);
                                        }
                                        if (!config.contains("settings.blocks."+blockName+".regions."+rName+"."+mobName+".attributes."+attName+".value")){
                                            valid = false;
                                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a mob with an attribute without a 'value' to it: "+mobName+"'s "+attName);
                                        }
                                    }
                                    if (!mobName.equals("ZOMBIE")){
                                        if (config.contains("settings.blocks."+blockName+".regions."+rName+"."+mobName+".attributes.zombiereinforcement")){
                                            valid = false;
                                            plugin.getServer().getConsoleSender().sendMessage("[MiG] Config Has a non-Zombie with Zombiereinforcement attribute: "+blockName+"->"+rName+"->"+mobName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return valid;
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
    public Set<String> getConfiguredMaterials(){
        if (config.contains("settings.blocks")){
            return config.getConfigurationSection("settings.blocks").getKeys(false);
        }
        return null;
    }
    public Integer getPlayerChance(Player player, String toBlock){
        String path;
        if (plugin.taskManager.isAttracting(player)){
            path = "settings.blocks."+toBlock+".chances.attract";
        } else if (plugin.taskManager.isRepelling(player)){
            path = "settings.blocks."+toBlock+".chances.repel";
        } else {
            path = "settings.blocks."+toBlock+".chances.base";
        }
        return config.getInt(path, 0);
    }
    public Set<String> getRegionNames(String toBlock){
        return config.getConfigurationSection("settings.blocks."+toBlock+".regions").getKeys(false);
    }
    public Set<String> getMobs(String toBlock, String regionName) {
        if (config.contains("settings.blocks."+toBlock+".regions."+regionName)){
            return config.getConfigurationSection("settings.blocks."+toBlock+".regions."+regionName).getKeys(false);
        }
        return null;
    }
    public int getMobWeight(String toBlock, String regionName, String mobName) {
        return config.getInt("settings.blocks."+toBlock+".regions."+regionName+"."+mobName+".weight", 0);
    }
    public boolean hasAttributes(String toBlock, String regionName, String mobName){
        return config.getConfigurationSection("settings.blocks."+toBlock+".regions."+regionName+"."+mobName).contains("attributes");
    }
    public int getAttributeChance(String toBlock, String regionName, String mobName, String attName) {
        return config.getInt("settings.blocks."+toBlock+".regions."+regionName+"."+mobName+".attributes."+attName+".chance", 0);
    }
    public int getAttributeValue(String toBlock, String regionName, String mobName, String attName){
        int def = 0;
        if (attName == "damage"){
            def = 2;
        } else if ( attName == "armor"){
            def = 0;
        } else if ( attName == "knockbackresistance"){
            def = 0;
        } else if (attName == "zombiereinforcement"){
            def = 0;
        } else if (attName == "health"){
            def = 20;
        } else if (attName == "movespeed"){
            def = 1;
        }
        return config.getInt("settings.blocks."+toBlock+".regions."+regionName+"."+mobName+".attributes."+attName+".value", def);
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
        if (config.contains("settings.blocks."+toBlock)){
            for (String regionName : config.getConfigurationSection("settings.blocks."+toBlock+".regions").getKeys(false)){
                for (String mobName : config.getConfigurationSection("settings.blocks."+toBlock+".regions."+regionName).getKeys(false)){
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
    
    public boolean removeEntityFromMaterial(String blockName, String mobName, String regionName){
        if (config.contains("settings.blocks."+blockName+".regions."+regionName+"."+mobName)){
            config.getConfigurationSection("settings.blocks."+blockName+".regions."+regionName).set(mobName, null);
            if (config.getConfigurationSection("settings.blocks."+blockName+".regions."+regionName).getKeys(false).size() == 0){
                removeRegionFromMaterial(blockName, regionName);
            }
            plugin.saveConfig();
            return true;
        }
        return false;
    }
    private void removeRegionFromMaterial(String blockName, String regionName){
        config.getConfigurationSection("settings.blocks."+blockName+".regions").set(regionName, null);
    }
    public boolean addEntityToMaterial(String blockName, String mobName, String regionName, Integer weight){
        if (config.contains("settings.blocks."+blockName+".regions."+regionName+"."+mobName+".weight")){
            if (config.getInt("settings.blocks."+blockName+".regions."+regionName+"."+mobName+".weight") == weight){
                return false;
            } else {
                config.set("settings.blocks."+blockName+".regions."+regionName+"."+mobName+".weight", weight);
                plugin.saveConfig();
                return false;
            }
        } else {
            config.set("settings.blocks."+blockName+".regions."+regionName+"."+mobName+".weight", weight);
            plugin.saveConfig();
            return true;
        }
    }
}
