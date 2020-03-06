package net.ak1cec0ld.plugins.MobsInGrass.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomYMLStorage {
    private File file;
    private YamlConfiguration yml;
    private String filePath;
    CustomYMLStorage(JavaPlugin plugin, String filename, String path){
        filePath = plugin.getDataFolder().getParent()+File.separator+path+File.separator+filename;
        file = new File(filePath);
        if(!file.exists()){
            try{
                plugin.saveResource(filename,false);
            } catch(IllegalArgumentException e){
                plugin.getLogger().warning(plugin.getName()+ " tried to create new file "+filename+ " and failed!");
            }
        }
        yml = YamlConfiguration.loadConfiguration(file);
        this.save();
    }
    //public File getFile(){
    //    return this.file;
    //}
    public YamlConfiguration getYamlConfiguration(){
        return this.yml;
    }
    //public void setYamlConfiguration(YamlConfiguration myYml){
    //    yml = myYml;
    //}
    public void save(){
        try{
            yml.save(file);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void reload(){
        file = new File(filePath);
        yml = YamlConfiguration.loadConfiguration(file);
        this.save();
    }
}
