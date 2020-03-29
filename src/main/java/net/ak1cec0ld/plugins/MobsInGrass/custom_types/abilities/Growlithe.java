package net.ak1cec0ld.plugins.MobsInGrass.custom_types.abilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Growlithe extends Ability{

    public Growlithe(){
        id = "growlithe";
        register();
    }

    @Override
    public void execute() {
        fillFireKeep();
    }

    private void fillFireKeep(){
        for( Entity each : entity.getNearbyEntities(3,1,3)){
            if(each instanceof Player) {
                Block b = each.getLocation().getBlock();
                ((Player)each).spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("Growlithe used FIRE FANG").color(ChatColor.RED).create());
                if (b.getType().equals(Material.AIR)) {
                    b.setType(Material.FIRE, false);
                    b.setType(Material.GLASS,false);
                }
                if(b.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
                    b.getRelative(BlockFace.UP).setType(Material.FIRE, false);
                    b.getRelative(BlockFace.UP).setType(Material.GLASS, false);
                }
            }
        }
    }
}
