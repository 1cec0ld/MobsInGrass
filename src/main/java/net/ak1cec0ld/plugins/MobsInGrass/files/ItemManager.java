package net.ak1cec0ld.plugins.MobsInGrass.files;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemManager {

    private static CustomYMLStorage yml;
    private static YamlConfiguration storage;
    public ItemManager(){
        yml = new CustomYMLStorage(MobsInGrass.instance(),"Items.yml","MobsInGrass");
        storage = yml.getYamlConfiguration();
        yml.save();

        initialize();
    }
    /*
      items:
        attract:
          ==: org.bukkit.inventory.ItemStack
          type: POTION
         meta:
            ==: ItemMeta
            meta-type: UNSPECIFIC
            display-name: §2Attract
            lore:
            - §An elixir that increases wild
            - §2encounters when drunk.
    */

    private void initialize(){

    }

}
