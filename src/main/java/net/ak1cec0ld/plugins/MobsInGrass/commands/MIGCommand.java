package net.ak1cec0ld.plugins.MobsInGrass.commands;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class MIGCommand implements TabExecutor {
    
    public MIGCommand(){
        MobsInGrass.instance().getServer().getPluginCommand("mig").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length){
            case 0:
                help(sender);
                break;
            case 1:
                switch (args[0].toLowerCase()){
                    case "reload":
                        sender.sendMessage("Reloading MobsInGrass...");
                        MobsInGrass.reload();
                        sender.sendMessage("Reloaded!");
                        break;
                    case "disable":
                        MobsInGrass.disable("Operator Command");
                        break;
                    case "enable":
                        MobsInGrass.enable();
                        break;
                    case "listmobs":
                        sender.sendMessage(MobProvider.list());
                        break;
                    case "time":
                        if(sender instanceof Player) {
                            sender.sendMessage(TimeManager.fromServerTime(((Player)sender).getWorld().getTime()));
                        }
                        break;
                    default:
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
        sender.sendMessage(ChatColor.GREEN+ "/MiG <reload,help,disable,enable,listmobs>");
    }
}