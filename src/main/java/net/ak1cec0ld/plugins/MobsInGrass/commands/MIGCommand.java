package net.ak1cec0ld.plugins.MobsInGrass.commands;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.CustomItem;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.items.ItemProvider;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs.MobProvider;
import net.ak1cec0ld.plugins.MobsInGrass.files.TimeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
            case 2:
                switch (args[0].toLowerCase()){
                    case "give":
                        CustomItem item = ItemProvider.getByDisplayName(args[1]);
                        if(item == null)break;
                        if(!(sender instanceof Player))break;
                        ((Player)sender).getInventory().addItem(item.getItem());
                        break;
                    default:
                        help(sender);
                }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        switch(args.length){
            case 1:
                return Arrays.asList("reload","help","disable","enable","listmobs","give");
            case 2:
                switch(args[0].toLowerCase()){
                    case "give":
                        return StringUtil.copyPartialMatches(args[1], new ArrayList<>(ItemProvider.getAllRegisteredNames()), completions);
                }
        }
        return null;
    }

    private void help(CommandSender sender){
        if (MobsInGrass.isDisabled()){
            sender.sendMessage(ChatColor.RED+""+ChatColor.BOLD+"MobsInGrass is disabled! Check the config and console!");
        }
        sender.sendMessage(ChatColor.AQUA+  "Version: 5.2");
        sender.sendMessage(ChatColor.GREEN+ "/MiG <reload,help,disable,enable,listmobs>");
        sender.sendMessage(ChatColor.GREEN+ "/MiG <give> (itemName)");
    }
}