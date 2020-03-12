package net.ak1cec0ld.plugins.MobsInGrass.custom_types.items;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Set;

public class ItemProvider {

    private static HashMap<String,CustomItem> registeredItems = new HashMap<>();


    public static void register(String id, CustomItem item){
        registeredItems.put(ChatColor.stripColor(id),item);
    }

    public static CustomItem getByDisplayName(String name){
        String replaced = ChatColor.stripColor(name.replace(' ','_'));
        for(CustomItem each : registeredItems.values()){
            if(ChatColor.stripColor(each.getDisplayName()).replace(' ','_').equalsIgnoreCase(replaced)){
                return each;
            }
        }
        return null;
    }

    public static Set<String> getAllRegisteredNames(){
        return registeredItems.keySet();
    }

}
