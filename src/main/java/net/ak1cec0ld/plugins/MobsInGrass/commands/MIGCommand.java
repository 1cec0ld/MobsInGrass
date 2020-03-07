package net.ak1cec0ld.plugins.MobsInGrass.commands;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import net.ak1cec0ld.plugins.MobsInGrass.files.MobsManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class MIGCommand implements TabExecutor {
    
    public MIGCommand(){}
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length){
            case 0:
                help(sender);
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
                    case "list":
                        sender.sendMessage(MobProvider.listMobs());
                        break;
                    default:
                        help(sender);
                }
                break;
            case 2:
                if (StringUtils.containsIgnoreCase(args[0], "search")){
                } else {
                    help(sender);
                }
                break;
            case 3:
                if (args[0].equalsIgnoreCase("remove")){
                } else {
                    help(sender);
                }
                break;
            case 4:
                if (args[0].equalsIgnoreCase("remove")){
                } else if (args[0].equalsIgnoreCase("add")){
                } else {
                    help(sender);
                }
                break;
            case 5:
                if (args[0].equalsIgnoreCase("add")){
                } else {
                    help(sender);
                }
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    private void help(CommandSender sender){
        if (MobsInGrass.isDisabled()){
            sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"MobsInGrass is disabled! Check the config and console!");
        }
        sender.sendMessage(ChatColor.AQUA+  "Version: 5");
        sender.sendMessage(ChatColor.GREEN+ "/MiG <reload,help,disable,enable,list>");
    }
}
/*
 *  /mig reload
 *  /mig blocks TALLGRASS
 *  /mig mobs ZOMBIE
 *  /mig TALLGRASS ZOMBIE 5
 *  /mig TALLGRASS route1 ZOMBIE
 */