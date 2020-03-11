package net.ak1cec0ld.plugins.MobsInGrass.custom_types.items;

import java.util.HashMap;
import java.util.Map;

public class ItemProvider {

    private static HashMap<String,CustomItem> registeredItems = new HashMap<>();


    public static void register(String id, CustomItem item){
        registeredItems.put(id,item);
    }

    public static CustomItem getByDisplayName(String name){
        for(Map.Entry<String,CustomItem> each : registeredItems.entrySet()){
            if(each.getValue().getDisplayName().equalsIgnoreCase(name.replace(' ', '_')))return each.getValue();
        }
        return null;
    }

}
