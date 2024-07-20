package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

public class RenderManager
{
    /** A map of entity classes and the associated renderer. */
    private Map entityRenderMap;

    /** The static instance of RenderManager. */
    public static RenderManager instance = new RenderManager();

    /** Renders fonts */
    private FontRenderer fontRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public RenderEngine renderEngine;
    public ItemRenderer itemRenderer;

    /** Reference to the World object. */
    public World worldObj;

    /** Rendermanager's variable for the player */
    public EntityLiving livingPlayer;
    public float playerViewY;
    public float playerViewX;

    /** Reference to the GameSettings object. */
    public GameSettings options;
    public double field_1222_l;
    public double field_1221_m;
    public double field_1220_n;

    private RenderManager()
    {
        entityRenderMap = new HashMap();
        entityRenderMap.put(EntitySpider.class, new RenderSpider());
        entityRenderMap.put(EntityCaveSpider.class, new RenderSpider());
        entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
        entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
        entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
        entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
        entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), 0.5F));
        entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
        entityRenderMap.put(EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
        entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish());
        entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
        entityRenderMap.put(EntityEnderman.class, new RenderEnderman());
        entityRenderMap.put(EntitySnowman.class, new RenderSnowMan());
        entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
        entityRenderMap.put(EntityBlaze.class, new RenderBlaze());
        entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
        entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube());
        entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
        entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6F));
        entityRenderMap.put(EntityGhast.class, new RenderGhast());
        entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
        entityRenderMap.put(EntityVillager.class, new RenderVillager());
        entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem());
        entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
        entityRenderMap.put(EntityDragon.class, new RenderDragon());
        entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal());
        entityRenderMap.put(Entity.class, new RenderEntity());
        entityRenderMap.put(EntityPainting.class, new RenderPainting());
        entityRenderMap.put(EntityArrow.class, new RenderArrow());
        entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball.getIconFromDamage(0)));
        entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(Item.enderPearl.getIconFromDamage(0)));
        entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(Item.eyeOfEnder.getIconFromDamage(0)));
        entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg.getIconFromDamage(0)));
        entityRenderMap.put(EntityPotion.class, new RenderSnowball(154));
        entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(Item.expBottle.getIconFromDamage(0)));
        entityRenderMap.put(EntityFireball.class, new RenderFireball(2.0F));
        entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(0.5F));
        entityRenderMap.put(EntityItem.class, new RenderItem());
        entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb());
        entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
        entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
        entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
        entityRenderMap.put(EntityBoat.class, new RenderBoat());
        entityRenderMap.put(EntityFishHook.class, new RenderFish());
        entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());
        Render render;

        for (Iterator iterator = entityRenderMap.values().iterator(); iterator.hasNext(); render.setRenderManager(this))
        {
            render = (Render)iterator.next();
        }
    }

    public Render getEntityClassRenderObject(Class par1Class)
    {
        Render render = (Render)entityRenderMap.get(par1Class);

        if (render == null && par1Class != (Entity.class))
        {
            render = getEntityClassRenderObject(par1Class.getSuperclass());
            entityRenderMap.put(par1Class, render);
        }

        return render;
    }

    public Render getEntityRenderObject(Entity par1Entity)
    {
        return getEntityClassRenderObject(par1Entity.getClass());
    }

    /**
     * Caches the current frame's active render info, including the current World, RenderEngine, GameSettings and
     * FontRenderer settings, as well as interpolated player position, pitch and yaw.
     */
    public void cacheActiveRenderInfo(World par1World, RenderEngine par2RenderEngine, FontRenderer par3FontRenderer, EntityLiving par4EntityLiving, GameSettings par5GameSettings, float par6)
    {
        worldObj = par1World;
        renderEngine = par2RenderEngine;
        options = par5GameSettings;
        livingPlayer = par4EntityLiving;
        fontRenderer = par3FontRenderer;

        if (par4EntityLiving.isPlayerSleeping())
        {
            int i = par1World.getBlockId(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));

            if (i == Block.bed.blockID)
            {
                int j = par1World.getBlockMetadata(MathHelper.floor_double(par4EntityLiving.posX), MathHelper.floor_double(par4EntityLiving.posY), MathHelper.floor_double(par4EntityLiving.posZ));
                int k = j & 3;
                playerViewY = k * 90 + 180;
                playerViewX = 0.0F;
            }
        }
        else
        {
            playerViewY = par4EntityLiving.prevRotationYaw + (par4EntityLiving.rotationYaw - par4EntityLiving.prevRotationYaw) * par6;
            playerViewX = par4EntityLiving.prevRotationPitch + (par4EntityLiving.rotationPitch - par4EntityLiving.prevRotationPitch) * par6;
        }

        if (par5GameSettings.thirdPersonView == 2)
        {
            playerViewY += 180F;
        }

        field_1222_l = par4EntityLiving.lastTickPosX + (par4EntityLiving.posX - par4EntityLiving.lastTickPosX) * (double)par6;
        field_1221_m = par4EntityLiving.lastTickPosY + (par4EntityLiving.posY - par4EntityLiving.lastTickPosY) * (double)par6;
        field_1220_n = par4EntityLiving.lastTickPosZ + (par4EntityLiving.posZ - par4EntityLiving.lastTickPosZ) * (double)par6;
    }

    /**
     * Will render the specified entity at the specified partial tick time. Args: entity, partialTickTime
     */
    public void renderEntity(Entity par1Entity, float par2)
    {
        double d = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        double d1 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        double d2 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;
        float f = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par2;
        int i = par1Entity.getBrightnessForRender(par2);

        if (par1Entity.isBurning())
        {
            i = 0xf000f0;
        }

        int j = i % 0x10000;
        int k = i / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderEntityWithPosYaw(par1Entity, d - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, par2);
    }

    /**
     * Renders the specified entity with the passed in position, yaw, and partialTickTime. Args: entity, x, y, z, yaw,
     * partialTickTime
     */
    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render render = getEntityRenderObject(par1Entity);

        if (render != null)
        {
            render.doRender(par1Entity, par2, par4, par6, par8, par9);
            render.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
        }
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(World par1World)
    {
        worldObj = par1World;
    }

    public double getDistanceToCamera(double par1, double par3, double par5)
    {
        double d = par1 - field_1222_l;
        double d1 = par3 - field_1221_m;
        double d2 = par5 - field_1220_n;
        return d * d + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }
}
