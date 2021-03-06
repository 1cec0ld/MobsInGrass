package net.ak1cec0ld.plugins.MobsInGrass.custom_types.mobs;

import net.ak1cec0ld.plugins.MobsInGrass.MobsInGrass;
import net.ak1cec0ld.plugins.MobsInGrass.custom_types.MySender;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class CustomMob {
    private static final double FAKE_NULL = Double.MIN_NORMAL;
    private static int recursionCounter = 0;

    private String identifier;
    private EntityType entityType;
    private Entity entity;
    private int amount = 1;
    private int size = 1;
    private boolean angry = false;
    private boolean aware = true;
    private boolean baby = false;
    private boolean glowing = false;
    private boolean gravity = true;
    private boolean invulnerable = false;
    private boolean killer = false;
    private boolean powered = false;
    private boolean silent = false;
    private double absorptionAmount = FAKE_NULL;
    private double armor = FAKE_NULL;
    private double armorToughness = FAKE_NULL;
    private double attackDamage = FAKE_NULL;
    private double attackSpeed = FAKE_NULL;
    private double flyingSpeed = FAKE_NULL;
    private double knockbackResist = FAKE_NULL;
    private double maxHealth = FAKE_NULL;
    private double movementSpeed = FAKE_NULL;
    private List<CustomMob> passengers;
    private String tags = "";
    private String announcement;
    private Set<Material> inBlocks;
    private Set<Material> onBlocks;
    private HashMap<String, ItemStack> items;
    private Set<PotionEffect> potionEffects;



    public CustomMob(String id, EntityType entityType){
        this.identifier = id;
        this.entityType = entityType;
        this.passengers = new ArrayList<>();
        this.inBlocks = new HashSet<>();
        this.onBlocks = new HashSet<>();
        this.items = new HashMap<>();
        this.potionEffects = new HashSet<>();
    }

    public String getIdentifier(){
        return identifier;
    }

    public void setAmount(int in){
        this.amount = in;
    }
    public void setSize(int in){
        this.size = in;
    }
    public void setBool(String attr, boolean in){
        switch (attr.toLowerCase()){
            case "angry":
                this.angry = in;
                break;
            case "aware":
                this.aware = in;
                break;
            case "baby":
                this.baby = in;
                break;
            case "glowing":
                this.glowing = in;
                break;
            case "gravity":
                this.gravity = in;
                break;
            case "invulnerable":
                this.invulnerable = in;
                break;
            case "killer":
                this.killer = in;
                break;
            case "powered":
                this.powered = in;
                break;
            case "silent":
                this.silent = in;
                break;
            default:
                MobsInGrass.debug("Invalid boolean attribute attempted to set: " + attr);
                MobsInGrass.disable("Use: aware,baby,glowing,gravity,invulnerable,powered,silent");
        }
    }
    public void setDouble(String attr, double in){
        switch (attr.toLowerCase()){
            case "absorption":
                this.absorptionAmount = in;
                break;
            case "armor":
                this.armor = in;
                break;
            case "armortoughness":
                this.armorToughness = in;
                break;
            case "attackdamage":
                this.attackDamage = in;
                break;
            case "attackspeed":
                this.attackSpeed = in;
                break;
            case "flyingspeed":
                this.flyingSpeed = in;
                break;
            case "knockbackresist":
                this.knockbackResist = in;
                break;
            case "maxhealth":
                this.maxHealth = in;
                break;
            case "movementspeed":
                this.movementSpeed = in;
                break;
            default:
                MobsInGrass.debug("Invalid double attribute attempted to set: " + attr);
                MobsInGrass.disable("Use: absorption,armor,armorToughness,attackDamage,attackSpeed,flyingSpeed,knockbackResist,maxHealth,movementSpeed");
        }
    }
    public void addItem(String type, ItemStack item){
        items.put(type, item);
    }
    public void addPassenger(CustomMob in){
        if(in == null){
            MobsInGrass.debug("Invalid or unregistered passenger attempted to set on: " + identifier);
            MobsInGrass.disable("Use: only existing mobs IDs that are defined, case-sensitive, and they must be above the 'vehicle' mob in the file.");
            return;
        }
        this.passengers.add(in);
    }
    public void addInMaterial(Material spawnIn){
        inBlocks.add(spawnIn);
    }
    public void addOnMaterial(Material spawnIn){
        onBlocks.add(spawnIn);
    }
    public void addPotionEffect(PotionEffect effect){
        potionEffects.add(effect);
    }
    public void setTags(String in){
        tags = in;
    }
    public void setAnnouncement(String in){
        announcement = in;
    }

    private Entity getEntity(){
        return entity;
    }

    public void spawn(Location where){
        announce(where,2);
        for(int i = 0; i < amount; i++) {
            entity = Objects.requireNonNull(where.getWorld()).spawnEntity(where, entityType);
            if (!(entity instanceof Mob)) {
                entity.remove();
                MobsInGrass.debug("Invalid entity attempted to spawn: " + entityType.name() + "; must be a Mob!");
                MobsInGrass.debug("https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Mob.html");
                return;
            }
            Mob mob = (Mob) entity;

            applyMobStats(mob);
        }
    }

    public boolean spawnsIn(Material check){
        return inBlocks.contains(check) ||
              (inBlocks.isEmpty() && check.equals(Material.GRASS));
    }
    public boolean spawnsOn(Material check){
        return onBlocks.contains(check);
    }

    private void announce(Location where, double howFarAway) {
        if(announcement == null) return;
        for (Entity each : Objects.requireNonNull(where.getWorld()).getNearbyEntities(where, howFarAway, howFarAway, howFarAway)) {
            each.sendMessage(ChatColor.translateAlternateColorCodes('&', announcement));
        }
    }

    private void applyMobStats(Mob mob){
        applyAttributes(mob);
        applyItems(mob);
        applyPotionEffects(mob);
        mob.setAware(aware);
        mob.setGlowing(glowing);
        mob.setGravity(gravity);
        mob.setInvulnerable(invulnerable);
        mob.setSilent(silent);


        if (absorptionAmount != FAKE_NULL) {
            mob.setAbsorptionAmount(absorptionAmount);
        }
        if (mob instanceof Ageable && baby){
            ((Ageable)mob).setBaby();
            ((Ageable)mob).setAgeLock(true);
            ((Ageable)mob).setBreed(false);
        }
        if (mob instanceof Creeper){
            ((Creeper)mob).setPowered(powered);
        }
        if (mob instanceof Rabbit && killer){
            ((Rabbit)mob).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        }
        if (mob instanceof Slime){
            ((Slime)mob).setSize(size);
        }
        if (mob instanceof Wolf){
            ((Wolf)mob).setAngry(angry);
        }
        if (mob instanceof Zombie){
            ((Zombie)mob).setBaby(baby);
        }
        if(tags != null) {
            applyTags(tags);
        }
        recursionCounter++;
        if(recursionCounter > 5)return;
        for(CustomMob each : passengers){
            each.spawn(mob.getLocation());
            mob.addPassenger(each.getEntity());
        }
        recursionCounter--;

    }
    private void applyAttributes(Mob mob){
        if(maxHealth != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(maxHealth);
            mob.setHealth(maxHealth);
        }
        if(armor != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_ARMOR) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(armor);
        }
        if(armorToughness != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)).setBaseValue(armor);
        }
        if(attackDamage != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(armor);
        }
        if(attackSpeed != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_ATTACK_SPEED) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(armor);
        }
        if(flyingSpeed != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_FLYING_SPEED) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_FLYING_SPEED)).setBaseValue(armor);
        }
        if(knockbackResist != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(armor);
        }
        if(movementSpeed != FAKE_NULL && mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null){
            Objects.requireNonNull(mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(armor);
        }
    }
    private void applyItems(Mob mob){
        if(items.containsKey("helmet")) {
            mob.getEquipment().setHelmet(items.get("helmet"));
        }
        if(items.containsKey("chestplate")) {
            mob.getEquipment().setChestplate(items.get("chestplate"));
        }
        if(items.containsKey("leggings")) {
            mob.getEquipment().setLeggings(items.get("leggings"));
        }
        if(items.containsKey("boots")) {
            mob.getEquipment().setBoots(items.get("boots"));
        }
        if(items.containsKey("mainhand")) {
            mob.getEquipment().setItemInMainHand(items.get("mainhand"));
        }
        if(items.containsKey("offhand")) {
            mob.getEquipment().setItemInOffHand(items.get("offhand"));
        }
    }
    private void applyPotionEffects(Mob mob){
        for(PotionEffect each : potionEffects){
            mob.addPotionEffect(each);
        }
    }

    private void applyTags(String commaDelimitedList){
        Boolean x = entity.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK);
        entity.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
        for(String tag : commaDelimitedList.split(",")){
            Bukkit.dispatchCommand(new MySender(), "tag " + entity.getUniqueId() + " add " + tag);
        }
        entity.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,x);
    }

    /*private NBTTagList getNBTTaglist(String commaDelimitedList){
        NBTTagList list = new NBTTagList();
        for(String each : commaDelimitedList.split(",")){
            list.add(NBTTagString.a(each));
        }

        return list;
    }*/
}
