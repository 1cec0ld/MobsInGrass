package com.gmail.ak1cec0ld.plugins.MobsInGrass;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AttractCommand implements CommandExecutor{
    
    private MobsInGrass plugin;
    
    public AttractCommand(MobsInGrass plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player)sender;
            PlayerInventory inv = player.getInventory();
            ItemStack mainhand = inv.getItemInMainHand();
            ItemStack offhand = inv.getItemInOffHand();
            Integer amount = plugin.getConfigManager().getAttractCount();
            if((mainhand.getType() == Material.valueOf(plugin.getConfigManager().getAttractMaterial())) && (mainhand.getAmount() >= amount)){
                mainhand.setAmount(mainhand.getAmount()-amount);
                inv.setItemInMainHand(mainhand);
                player.sendMessage(ChatColor.DARK_RED + "You feel the effects of the attract...");
                plugin.getTaskManager().attract(player,plugin.getConfigManager().getAttractDuration());
            } else if ((offhand.getType() == Material.valueOf(plugin.getConfigManager().getAttractMaterial())) && (offhand.getAmount() >= amount)){
                offhand.setAmount(offhand.getAmount()-amount);
                inv.setItemInOffHand(offhand);
                player.sendMessage(ChatColor.DARK_RED + "You feel the effects of the attract...");
                plugin.getTaskManager().attract(player,plugin.getConfigManager().getAttractDuration());
            } else {
                player.sendMessage(ChatColor.DARK_RED + "You must be holding at least "+amount+" "+plugin.getConfigManager().getAttractMaterial().toString()+" to use Attract!");
            }
        } else {
            plugin.getServer().getConsoleSender().sendMessage("Error, Player Senders Only.");
        }
        return true;
    }
}
