package net.ak1cec0ld.plugins.MobsInGrass.commands;

import java.io.File;
import java.util.List;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.files.MobsManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import org.apache.commons.lang.StringUtils;

public class MIGCommand implements TabExecutor {

    private MobsInGrass plugin;
    
    public MIGCommand(MobsInGrass plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length){
            case 0:
                if (sender instanceof Player){
                    ((Player)sender).performCommand("mig help");
                }
                break;
            case 1:
                switch (args[0].toLowerCase()){
                    case "reload":
                        MobsManager.reload();
                        break;
                    case "disable":
                        MobsInGrass.disable();
                        break;
                    case "enable":
                        MobsInGrass.enable();
                        break;
                    default:
                        if (plugin.isDisabled()){
                            sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"MobsInGrass is disabled! Check the config and console!");
                        }
                        sender.sendMessage(ChatColor.AQUA+  "Version: 5");
                        sender.sendMessage(ChatColor.GREEN+ "/MiG <reload,help,disable,enable>");
                }
                break;
            case 2:
                if (StringUtils.containsIgnoreCase(args[0], "search")){
                } else {
                    ((Player)sender).performCommand("mig help");
                }
                break;
            case 3:
                if (args[0].equalsIgnoreCase("remove")){
                } else {
                    ((Player)sender).performCommand("mig help");
                }
                break;
            case 4:
                if (args[0].equalsIgnoreCase("remove")){
                } else if (args[0].equalsIgnoreCase("add")){
                } else {
                    ((Player)sender).performCommand("mig help");
                }
                break;
            case 5:
                if (args[0].equalsIgnoreCase("add")){
                } else {
                    ((Player)sender).performCommand("mig help");
                }
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
/*
 *  /mig reload
 *  /mig blocks TALLGRASS
 *  /mig mobs ZOMBIE
 *  /mig TALLGRASS ZOMBIE 5
 *  /mig TALLGRASS route1 ZOMBIE
 */