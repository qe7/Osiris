package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class EntityList
{
    /** Provides a mapping between entity classes and a string */
    private static Map stringToClassMapping = new HashMap();

    /** Provides a mapping between a string and an entity classes */
    private static Map classToStringMapping = new HashMap();

    /** provides a mapping between an entityID and an Entity Class */
    private static Map IDtoClassMapping = new HashMap();

    /** provides a mapping between an Entity Class and an entity ID */
    private static Map classToIDMapping = new HashMap();

    /** Maps entity names to their numeric identifiers */
    private static Map stringToIDMapping = new HashMap();

    /** This is a HashMap of the Creative Entity Eggs/Spawners. */
    public static HashMap entityEggs = new HashMap();

    public EntityList()
    {
    }

    /**
     * adds a mapping between Entity classes and both a string representation and an ID
     */
    private static void addMapping(Class par0Class, String par1Str, int par2)
    {
        stringToClassMapping.put(par1Str, par0Class);
        classToStringMapping.put(par0Class, par1Str);
        IDtoClassMapping.put(Integer.valueOf(par2), par0Class);
        classToIDMapping.put(par0Class, Integer.valueOf(par2));
        stringToIDMapping.put(par1Str, Integer.valueOf(par2));
    }

    /**
     * Adds a entity mapping with egg info.
     */
    private static void addMapping(Class par0Class, String par1Str, int par2, int par3, int par4)
    {
        addMapping(par0Class, par1Str, par2);
        entityEggs.put(Integer.valueOf(par2), new EntityEggInfo(par2, par3, par4));
    }

    /**
     * Create a new instance of an entity in the world by using the entity name.
     */
    public static Entity createEntityByName(String par0Str, World par1World)
    {
        Entity entity = null;

        try
        {
            Class class1 = (Class)stringToClassMapping.get(par0Str);

            if (class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[]
                        {
                            World.class
                        }).newInstance(new Object[]
                                {
                                    par1World
                                });
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return entity;
    }

    /**
     * create a new instance of an entity from NBT store
     */
    public static Entity createEntityFromNBT(NBTTagCompound par0NBTTagCompound, World par1World)
    {
        Entity entity = null;

        try
        {
            Class class1 = (Class)stringToClassMapping.get(par0NBTTagCompound.getString("id"));

            if (class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[]
                        {
                            World.class
                        }).newInstance(new Object[]
                                {
                                    par1World
                                });
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (entity != null)
        {
            entity.readFromNBT(par0NBTTagCompound);
        }
        else
        {
            System.out.println((new StringBuilder()).append("Skipping Entity with id ").append(par0NBTTagCompound.getString("id")).toString());
        }

        return entity;
    }

    /**
     * Create a new instance of an entity in the world by using an entity ID.
     */
    public static Entity createEntityByID(int par0, World par1World)
    {
        Entity entity = null;

        try
        {
            Class class1 = (Class)IDtoClassMapping.get(Integer.valueOf(par0));

            if (class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[]
                        {
                            World.class
                        }).newInstance(new Object[]
                                {
                                    par1World
                                });
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (entity == null)
        {
            System.out.println((new StringBuilder()).append("Skipping Entity with id ").append(par0).toString());
        }

        return entity;
    }

    /**
     * gets the entityID of a specific entity
     */
    public static int getEntityID(Entity par0Entity)
    {
        return ((Integer)classToIDMapping.get(par0Entity.getClass())).intValue();
    }

    /**
     * Gets the string representation of a specific entity.
     */
    public static String getEntityString(Entity par0Entity)
    {
        return (String)classToStringMapping.get(par0Entity.getClass());
    }

    /**
     * Finds the class using IDtoClassMapping and classToStringMapping
     */
    public static String getStringFromID(int par0)
    {
        Class class1 = (Class)IDtoClassMapping.get(Integer.valueOf(par0));

        if (class1 != null)
        {
            return (String)classToStringMapping.get(class1);
        }
        else
        {
            return null;
        }
    }

    static
    {
        addMapping(EntityItem.class, "Item", 1);
        addMapping(EntityXPOrb.class, "XPOrb", 2);
        addMapping(EntityPainting.class, "Painting", 9);
        addMapping(EntityArrow.class, "Arrow", 10);
        addMapping(EntitySnowball.class, "Snowball", 11);
        addMapping(EntityFireball.class, "Fireball", 12);
        addMapping(EntitySmallFireball.class, "SmallFireball", 13);
        addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
        addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
        addMapping(EntityPotion.class, "ThrownPotion", 16);
        addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
        addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
        addMapping(EntityFallingSand.class, "FallingSand", 21);
        addMapping(EntityMinecart.class, "Minecart", 40);
        addMapping(EntityBoat.class, "Boat", 41);
        addMapping(EntityLiving.class, "Mob", 48);
        addMapping(EntityMob.class, "Monster", 49);
        addMapping(EntityCreeper.class, "Creeper", 50, 0xda70b, 0);
        addMapping(EntitySkeleton.class, "Skeleton", 51, 0xc1c1c1, 0x494949);
        addMapping(EntitySpider.class, "Spider", 52, 0x342d27, 0xa80e0e);
        addMapping(EntityGiantZombie.class, "Giant", 53);
        addMapping(EntityZombie.class, "Zombie", 54, 44975, 0x799c65);
        addMapping(EntitySlime.class, "Slime", 55, 0x51a03e, 0x7ebf6e);
        addMapping(EntityGhast.class, "Ghast", 56, 0xf9f9f9, 0xbcbcbc);
        addMapping(EntityPigZombie.class, "PigZombie", 57, 0xea9393, 0x4c7129);
        addMapping(EntityEnderman.class, "Enderman", 58, 0x161616, 0);
        addMapping(EntityCaveSpider.class, "CaveSpider", 59, 0xc424e, 0xa80e0e);
        addMapping(EntitySilverfish.class, "Silverfish", 60, 0x6e6e6e, 0x303030);
        addMapping(EntityBlaze.class, "Blaze", 61, 0xf6b201, 0xfff87e);
        addMapping(EntityMagmaCube.class, "LavaSlime", 62, 0x340000, 0xfcfc00);
        addMapping(EntityDragon.class, "EnderDragon", 63);
        addMapping(EntityPig.class, "Pig", 90, 0xf0a5a2, 0xdb635f);
        addMapping(EntitySheep.class, "Sheep", 91, 0xe7e7e7, 0xffb5b5);
        addMapping(EntityCow.class, "Cow", 92, 0x443626, 0xa1a1a1);
        addMapping(EntityChicken.class, "Chicken", 93, 0xa1a1a1, 0xff0000);
        addMapping(EntitySquid.class, "Squid", 94, 0x223b4d, 0x708899);
        addMapping(EntityWolf.class, "Wolf", 95, 0xd7d3d3, 0xceaf96);
        addMapping(EntityMooshroom.class, "MushroomCow", 96, 0xa00f10, 0xb7b7b7);
        addMapping(EntitySnowman.class, "SnowMan", 97);
        addMapping(EntityOcelot.class, "Ozelot", 98, 0xefde7d, 0x564434);
        addMapping(EntityIronGolem.class, "VillagerGolem", 99);
        addMapping(EntityVillager.class, "Villager", 120, 0x563c33, 0xbd8b72);
        addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
    }
}
