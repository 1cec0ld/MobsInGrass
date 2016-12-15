package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.apache.commons.lang.StringUtils;

public class MIGCommand implements CommandExecutor {

    private MobsInGrass plugin;
    
    public MIGCommand(MobsInGrass plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0){
            if (sender instanceof Player){
                ((Player)sender).performCommand("mig help");
            }
            plugin.getServer().getConsoleSender().sendMessage(plugin.configManager.getVersion());
        } else if (args.length == 1){
            if (args[0].equalsIgnoreCase("reload")){
                plugin.configManager.setConfig(YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml")));
                if (plugin.configManager.validateConfig()){
                    sender.sendMessage(ChatColor.GREEN+"[MiG] reloaded!");
                    plugin.disabled = false;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED+"[MiG] reloaded with errors in config.");
                    plugin.disabled = true;
                }
            } else {
                if (plugin.disabled){
                    sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"MobsInGrass is disabled! Check the config and console!");
                }
                sender.sendMessage(ChatColor.AQUA+  "Version: "+ChatColor.BLUE+ plugin.configManager.getVersion());
                sender.sendMessage(ChatColor.GREEN+ "/MiG <reload,help>");
                sender.sendMessage(ChatColor.GREEN+ "/MiG <search> <BlockType,MobType>");
                sender.sendMessage(ChatColor.RED+   "/MiG <remove> <BlockType> <MobType> [WGRegion]");
                sender.sendMessage(ChatColor.RED+   "/MiG <add> <BlockType> <base,attract,repel> <#weight>");
                sender.sendMessage(ChatColor.RED+   "/MiG <add> <BlockType> <MobType> <#weight> [WGRegion]");
            }
        } else if (args.length == 2){
            if (StringUtils.containsIgnoreCase(args[0], "search")){
                if(plugin.isEntity(args[1])){
                    for (String str: plugin.configManager.getMaterialsForThisMob(args[1].toUpperCase())){
                        sender.sendMessage(str);
                    }
                } else if(plugin.isMaterial(args[1])){
                    for (String stri : plugin.configManager.getMobsForThisMaterial(args[1].toUpperCase())){
                        sender.sendMessage(stri);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED+args[1]+" is not a valid Entity or Block name!");
                }
            } else {
                ((Player)sender).performCommand("mig help");
            }
        } else if (args.length == 3){
            if (args[0].equalsIgnoreCase("remove")){
                if (plugin.isMaterial(args[1])){
                    if (plugin.isEntity(args[2])){
                        if (plugin.configManager.removeEntityFromMaterial(args[1],args[2],"default")){
                            sender.sendMessage(ChatColor.GREEN+args[2]+" was removed from "+args[1]+" for region "+ChatColor.ITALIC+"default");
                        } else {
                            sender.sendMessage(ChatColor.RED+args[2]+" didn't exist in "+args[1]+" for region "+ChatColor.ITALIC+"default");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED+args[2]+" is not a valid Entity Type!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED+args[2]+" is not a valid Block Type!");
                }
            } else {
                ((Player)sender).performCommand("mig help");
            }
        } else if (args.length == 4){
            if (args[0].equalsIgnoreCase("remove")){
                if (plugin.isMaterial(args[1])){
                    if (plugin.isEntity(args[2])){
                        if (plugin.configManager.removeEntityFromMaterial(args[1],args[2],args[3])){
                            sender.sendMessage(ChatColor.GREEN+args[2]+" was removed from "+args[1]+" for region "+ChatColor.ITALIC+args[3]);
                        } else {
                            sender.sendMessage(ChatColor.RED+args[2]+" didn't exist for "+args[1]+" for region "+ChatColor.ITALIC+"default");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED+args[2]+" is not a valid Entity Type!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED+args[2]+" is not a valid Block Type!");
                }
            } else if (args[0].equalsIgnoreCase("add")){
                if (plugin.isMaterial(args[1])){
                    if (args[2].equalsIgnoreCase("base")){
                        
                    } else if (args[2].equalsIgnoreCase("attract")){
                        
                    } else if (args[2].equalsIgnoreCase("repel")){
                        
                    } else if (plugin.isEntity(args[2])){
                        //add to region default
                        if (plugin.isInteger(args[3])){
                            if(plugin.configManager.addEntityToMaterial(args[1], args[2], "default", Integer.parseInt(args[3]))){
                                sender.sendMessage(ChatColor.GREEN+args[2]+" will now spawn in region "+ChatColor.ITALIC+"default"+ChatColor.RESET+" "+ChatColor.GREEN+" when walking in "+args[1]);
                            } else {
                                sender.sendMessage(ChatColor.RED+args[2]+" overwrote an existing entry in region "+ChatColor.ITALIC+"default"+ChatColor.RESET+" "+ChatColor.GREEN+" when walking in "+args[1]);
                            }
                        } else {
                            ((Player)sender).performCommand("mig help");
                        }
                    } else {
                        ((Player)sender).performCommand("mig help");
                    }
                } else {
                    ((Player)sender).performCommand("mig help");
                }
            } else {
                ((Player)sender).performCommand("mig help");
            }
        } else if (args.length == 5){
            if (args[0].equalsIgnoreCase("add")){
                if (plugin.isMaterial(args[1])){
                    if (plugin.isEntity(args[2])){
                        //add to region named
                        if (plugin.isInteger(args[3])){
                            if(plugin.configManager.addEntityToMaterial(args[1], args[2], args[4], Integer.parseInt(args[3]))){
                                sender.sendMessage(ChatColor.GREEN+args[2]+" will now spawn in region "+ChatColor.ITALIC+"default"+ChatColor.RESET+" "+ChatColor.GREEN+" when walking in "+args[1]);
                            } else {
                                sender.sendMessage(ChatColor.RED+args[2]+" overwrote an existing entry in region "+ChatColor.ITALIC+args[4]+ChatColor.RESET+" "+ChatColor.GREEN+" when walking in "+args[1]);
                            }
                        } else {
                            ((Player)sender).performCommand("mig help");
                        }
                    } else {
                        ((Player)sender).performCommand("mig help");
                    }
                } else {
                    ((Player)sender).performCommand("mig help");
                }
            } else {
                ((Player)sender).performCommand("mig help");
            }
        }
        return true;
    }

}
/*
 *  /mig reload
 *  /mig blocks TALLGRASS
 *  /mig mobs ZOMBIE
 *  /mig TALLGRASS ZOMBIE 5
 *  /mig TALLGRASS route1 ZOMBIE
 */