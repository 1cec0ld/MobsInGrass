package net.ak1cec0ld.plugins.MobsInGrass.commands;

import java.util.ArrayList;
import java.util.List;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleteManager implements TabCompleter{

    private MobsInGrass plugin;
    
    public TabCompleteManager(MobsInGrass plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("mig")){
            List<String> suggestions = new ArrayList<>();
            if (args.length == 1){
                suggestions.add("search");
                suggestions.add("add");
                suggestions.add("remove");
                suggestions.add("reload");
                suggestions.add("help");
            } else if (args.length == 2){
                if (StringUtils.containsIgnoreCase(args[0], "search")){
                    for (String c : plugin.getConfigManager().getConfiguredMaterials()){
                        if (!suggestions.contains(c)){
                            suggestions.add(c);
                        }
                    }
                    for (Material v : Material.values()){
                        //plugin.getServer().broadcastMessage("0 "+v.toString());
                        for (String stri : plugin.getConfigManager().getMobsForThisMaterial(v.toString())){
                            if (!suggestions.contains(stri) && !stri.contains("can spawn")){
                                suggestions.add(stri);
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")){
                    for (String c : plugin.getConfigManager().getConfiguredMaterials()){
                        if (!suggestions.contains(c)){
                            suggestions.add(c);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")){
                    suggestions.add("AIR");
                    suggestions.add("SAPLING");
                    suggestions.add("FLOWING_WATER");
                    suggestions.add("WATER");
                    suggestions.add("FLOWING_LAVA");
                    suggestions.add("LAVA");
                    suggestions.add("GOLDEN_RAIL");
                    suggestions.add("DETECTOR_RAIL");
                    suggestions.add("WEB");
                    suggestions.add("LONG_GRASS");
                    suggestions.add("DOUBLE_PLANT");
                    suggestions.add("DEAD_BUSH");
                    suggestions.add("RED_ROSE");
                    suggestions.add("YELLOW_FLOWER");
                    suggestions.add("BROWN_MUSHOOM");
                    suggestions.add("RED_MUSHROOM");
                    suggestions.add("STONE_SLAB");
                    suggestions.add("TORCH");
                    suggestions.add("FIRE");
                    suggestions.add("WOOD_STAIRS");
                    suggestions.add("REDSTONE_WIRE");
                    suggestions.add("STANDING_SIGN");
                    suggestions.add("STONE_PRESSURE_PLATE");
                    suggestions.add("WOODEN_PRESSURE_PLATE");
                    suggestions.add("REEDS");
                }
            }
            if (suggestions.isEmpty()){
                return null;
            } else {
                return suggestions;
            }
        }
        return null;
    }

}
