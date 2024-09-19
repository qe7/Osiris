package lunatrius.schematica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import lunatrius.utils.Vector3f;
import lunatrius.utils.Vector3i;
import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Frustrum;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class Render
{
    private final Settings settings = Settings.instance();
    private final Map glLists = new HashMap();
    private final java.util.List textures = new ArrayList();
    private final BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
    private Field fieldTextureMap;
    private Field fieldSingleIntBuffer;
    
    public static final Render INSTANCE = new Render();
    
    public Render()
    {
        fieldTextureMap = null;
        fieldSingleIntBuffer = null;
        initTexture();
        initReflection();
    }

    public void initTexture()
    {
        Graphics g = missingTextureImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 64, 64);
        g.setColor(Color.BLACK);
        g.drawString("missingtex", 1, 10);
        g.dispose();
    }

    public void initReflection()
    {
        try
        {
            fieldTextureMap = (net.minecraft.src.RenderEngine.class).getDeclaredField("c");
            fieldTextureMap.setAccessible(true);
            fieldSingleIntBuffer = (net.minecraft.src.RenderEngine.class).getDeclaredField("f");
            fieldSingleIntBuffer.setAccessible(true);
        }
        catch (Exception exception)
        {
            fieldTextureMap = null;
            fieldSingleIntBuffer = null;

            try
            {
                fieldTextureMap = (net.minecraft.src.RenderEngine.class).getDeclaredField("textureMap");
                fieldTextureMap.setAccessible(true);
                fieldSingleIntBuffer = (net.minecraft.src.RenderEngine.class).getDeclaredField("singleIntBuffer");
                fieldSingleIntBuffer.setAccessible(true);
            }
            catch (Exception exception1)
            {
                exception1.printStackTrace();
                fieldTextureMap = null;
                fieldSingleIntBuffer = null;
                settings.enableAlpha = false;
            }
        }
    }

    public void onRenderWorldLast(RenderGlobal renderglobal, float f)
    {
        if (settings.minecraft != null)
        {
            EntityPlayerSP entityplayersp = settings.minecraft.thePlayer;

            if (entityplayersp != null)
            {
                settings.playerPosition.x = (float)(entityplayersp.lastTickPosX + (entityplayersp.posX - entityplayersp.lastTickPosX) * (double)f);
                settings.playerPosition.y = (float)(entityplayersp.lastTickPosY + (entityplayersp.posY - entityplayersp.lastTickPosY) * (double)f);
                settings.playerPosition.z = (float)(entityplayersp.lastTickPosZ + (entityplayersp.posZ - entityplayersp.lastTickPosZ) * (double)f);
                settings.rotationRender = (int)(((entityplayersp.rotationYaw / 90F) % 4F + 4F) % 4F);
                render();
            }
        }
    }

    private void render()
    {
        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        if (settings.isRenderingSchematic && settings.schematic != null)
        {
            GL11.glTranslatef(-settings.getTranslationX(), -settings.getTranslationY(), -settings.getTranslationZ());
            renderSchematic();
            GL11.glTranslatef(settings.getTranslationX(), settings.getTranslationY(), settings.getTranslationZ());
        }

        if (settings.isRenderingGuide)
        {
            GL11.glTranslatef(-settings.playerPosition.x, -settings.playerPosition.y, -settings.playerPosition.z);
            renderGuide();
            GL11.glTranslatef(settings.playerPosition.x, settings.playerPosition.y, settings.playerPosition.z);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private void renderSchematic()
    {
        int i = (int)settings.getTranslationX();
        int j = (int)settings.getTranslationY() - 1;
        int k = (int)settings.getTranslationZ();
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        int i2 = 0;

        if (settings.renderingLayer < 0)
        {
            settings.renderBlocks.renderAllFaces = false;
            l = Math.max(i - settings.renderRange.x, 0);
            i1 = Math.min(i + settings.renderRange.x, settings.schematic.width());
            j1 = Math.max(j - settings.renderRange.y, 0);
            k1 = Math.min(j + settings.renderRange.y, settings.schematic.height());
            l1 = Math.max(k - settings.renderRange.z, 0);
            i2 = Math.min(k + settings.renderRange.z, settings.schematic.length());
        }
        else
        {
            settings.renderBlocks.renderAllFaces = true;
            l = Math.max(i - settings.renderRange.x * settings.renderRange.y, 0);
            i1 = Math.min(i + settings.renderRange.x * settings.renderRange.y, settings.schematic.width());
            j1 = settings.renderingLayer;
            k1 = settings.renderingLayer + 1;
            l1 = Math.max(k - settings.renderRange.z * settings.renderRange.y, 0);
            i2 = Math.min(k + settings.renderRange.z * settings.renderRange.y, settings.schematic.length());
        }
        
        l = 0;
        i1 = settings.schematic.width();
        j1 = 0;
        k1 = settings.schematic.height();
        l1 = 0;
        i2 = settings.schematic.length();
        
        renderSchematic(l, j1, l1, i1, k1, i2);
    }

    private void renderSchematic(int i, int j, int k, int l, int i1, int j1)
    {
        Tessellator.instance.startDrawingQuads();
        SchematicWorld schematicworld = settings.schematic;
        RenderBlocks renderblocks = settings.renderBlocks;
        RenderTileEntity rendertileentity = settings.renderTileEntity;
        World world = settings.minecraft.theWorld;
        ArrayList arraylist = new ArrayList();
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        Object obj = null;
        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(settings.playerPosition.x - (float)settings.offset.x, settings.playerPosition.y - (float)settings.offset.y, settings.playerPosition.z - (float)settings.offset.z);
        frustrum.setPosition(0.0D, 0.0D, 0.0D);
        boolean flag = false;
        boolean flag1 = false;
        Object obj1 = null;
        String s = "";

        for (int k1 = i; k1 < l; k1++)
        {
            for (int i2 = j; i2 < i1; i2++)
            {
                for (int k2 = k; k2 < j1; k2++)
                {
                    try
                    {
                        AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(k1, i2, k2, k1 + 1, i2 + 1, k2 + 1);

                        if (!frustrum.isBoundingBoxInFrustum(axisalignedbb))
                        {
                            continue;
                        }

                        int i3 = schematicworld.getBlockId(k1, i2, k2);
                        int k3 = world.getBlockId(k1 + settings.offset.x, i2 + settings.offset.y, k2 + settings.offset.z);

                        if (k3 != 0)
                        {
                            if (!settings.highlight)
                            {
                                continue;
                            }

                            if (i3 != k3)
                            {
                                arraylist.add(new Vector3i(k1, i2, k2));
                                continue;
                            }

                            if (schematicworld.getBlockMetadata(k1, i2, k2) != world.getBlockMetadata(k1 + settings.offset.x, i2 + settings.offset.y, k2 + settings.offset.z))
                            {
                                arraylist1.add(new Vector3i(k1, i2, k2));
                            }

                            continue;
                        }

                        Block block;

                        if (k3 != 0 || i3 <= 0 || i3 >= 4096 || (block = Block.blocksList[i3]) == null)
                        {
                            continue;
                        }

                        if (settings.renderingLayer >= 0 && (i3 == Block.redstoneRepeaterActive.blockID || i3 == Block.redstoneRepeaterIdle.blockID))
                        {
                            renderblocks.renderAllFaces = false;
                        }

                        if (settings.highlight)
                        {
                            arraylist2.add(new Vector3i(k1, i2, k2));
                        }

                        renderblocks.renderBlockByRenderType(block, k1, i2, k2);

                        if (settings.renderingLayer >= 0 && (i3 == Block.redstoneRepeaterActive.blockID || i3 == Block.redstoneRepeaterIdle.blockID))
                        {
                            renderblocks.renderAllFaces = true;
                        }
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
            }
        }

        Tessellator.instance.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, settings.alpha);

        try
        {
            Iterator iterator = schematicworld.getTileEntities().iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                TileEntity tileentity = (TileEntity)iterator.next();
                int l1 = tileentity.xCoord;
                int j2 = tileentity.yCoord;
                int l2 = tileentity.zCoord;
                AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox(l1, j2, l2, l1 + 1, j2 + 1, l2 + 1);

                if (frustrum.isBoundingBoxInFrustum(axisalignedbb1) && l1 >= i && l1 < l && l2 >= k && l2 < j1 && j2 >= j && j2 < i1)
                {
                    int j3 = schematicworld.getBlockId(l1, j2, l2);
                    int l3 = world.getBlockId(l1 + settings.offset.x, j2 + settings.offset.y, l2 + settings.offset.z);

                    if (l3 != 0 && (j3 != l3 || schematicworld.getBlockMetadata(l1, j2, l2) != world.getBlockMetadata(l1 + settings.offset.x, j2 + settings.offset.y, l2 + settings.offset.z)))
                    {
                        if (settings.highlight)
                        {
                            arraylist.add(new Vector3i(l1, j2, l2));
                        }
                    }
                    else if (l3 == 0 && j3 > 0 && j3 < 4096)
                    {
                        if (tileentity instanceof TileEntitySign)
                        {
                            rendertileentity.renderTileEntitySignAt((TileEntitySign)tileentity);
                        }
                        else if (tileentity instanceof TileEntityChest)
                        {
                            rendertileentity.renderTileEntityChestAt((TileEntityChest)tileentity);
                        }
                        else
                        {
                            TileEntitySpecialRenderer tileentityspecialrenderer = TileEntityRenderer.instance.getSpecialRendererForEntity(tileentity);

                            if (tileentityspecialrenderer != null)
                            {
                                tileentityspecialrenderer.renderTileEntityAt(tileentity, l1, j2, l2, 0.0F);
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, settings.alpha);
                            }
                        }
                    }
                }
            }
            while (true);
        }
        catch (Exception exception1)
        {
            exception1.printStackTrace();
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5F);
        GL11.glColor4f(0.75F, 0.0F, 0.75F, 0.25F);
        drawCuboid(Vector3i.ZERO, new Vector3i(schematicworld.width(), schematicworld.height(), schematicworld.length()), 1);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.25F);
        Vector3i vector3i;

        for (Iterator iterator1 = arraylist.iterator(); iterator1.hasNext(); drawCuboid(vector3i, vector3i.clone().add(1), 3))
        {
            vector3i = (Vector3i)iterator1.next();
        }

        GL11.glColor4f(0.75F, 0.35F, 0.0F, 0.45F);
        Vector3i vector3i1;

        for (Iterator iterator2 = arraylist1.iterator(); iterator2.hasNext(); drawCuboid(vector3i1, vector3i1.clone().add(1), 3))
        {
            vector3i1 = (Vector3i)iterator2.next();
        }

        GL11.glColor4f(0.0F, 0.75F, 1.0F, 0.25F);
        Vector3i vector3i2;

        for (Iterator iterator3 = arraylist2.iterator(); iterator3.hasNext(); drawCuboid(vector3i2, vector3i2.clone().add(1), 3))
        {
            vector3i2 = (Vector3i)iterator3.next();
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void renderGuide()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(1.5F);
        GL11.glColor4f(0.0F, 0.75F, 0.0F, 0.25F);
        drawCuboid(settings.pointMin, settings.pointMax.clone().add(1), 1);
        GL11.glColor4f(0.75F, 0.0F, 0.0F, 0.25F);
        drawCuboid(settings.pointA, settings.pointA.clone().add(1), 3);
        GL11.glColor4f(0.0F, 0.0F, 0.75F, 0.25F);
        drawCuboid(settings.pointB, settings.pointB.clone().add(1), 3);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawCuboid(Vector3i vector3i, Vector3i vector3i1, int i)
    {
        Vector3f vector3f = (new Vector3f()).sub(settings.blockDelta);
        Vector3f vector3f1 = (new Vector3f(vector3i1.x, vector3i1.y, vector3i1.z)).sub(vector3i.x, vector3i.y, vector3i.z).add(settings.blockDelta);
        String s = (new StringBuilder()).append(vector3f1.x).append("/").append(vector3f1.y).append("/").append(vector3f1.z).append("/").append(i).toString();

        if (!glLists.containsKey(s))
        {
            glLists.put(s, Integer.valueOf(compileList(vector3f, vector3f1, i)));
        }

        GL11.glTranslatef(vector3i.x, vector3i.y, vector3i.z);
        GL11.glCallList(((Integer)glLists.get(s)).intValue());
        GL11.glTranslatef(-vector3i.x, -vector3i.y, -vector3i.z);
    }

    private int compileList(Vector3f vector3f, Vector3f vector3f1, int i)
    {
        int j = GL11.glGenLists(1);
        GL11.glNewList(j, GL11.GL_COMPILE);

        if ((i & 1) != 0)
        {
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glEnd();
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glEnd();
        }

        if ((i & 2) != 0)
        {
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f.z);
            GL11.glVertex3f(vector3f.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f1.y, vector3f1.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f.z);
            GL11.glVertex3f(vector3f1.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f1.z);
            GL11.glVertex3f(vector3f.x, vector3f.y, vector3f.z);
            GL11.glEnd();
        }

        GL11.glEndList();
        return j;
    }

    private String getTextureName(String s)
    {
        if (!settings.enableAlpha)
        {
            return s;
        }

        String s1 = (new StringBuilder()).append("/").append((int)(settings.alpha * 255F)).append(s.replace('/', '-')).toString();

        if (textures.contains(s1))
        {
            return s1;
        }

        try
        {
            TexturePackBase texturepackbase = settings.minecraft.texturePackList.selectedTexturePack;
            File file = new File(Settings.textureDirectory, (new StringBuilder()).append(texturepackbase.texturePackFileName.replace(".zip", "")).append(s1).toString());

            if (!file.exists())
            {
                BufferedImage bufferedimage = readTextureImage(texturepackbase.getResourceAsStream(s));

                if (bufferedimage == null)
                {
                    return s;
                }

                for (int i = 0; i < bufferedimage.getWidth(); i++)
                {
                    for (int j = 0; j < bufferedimage.getHeight(); j++)
                    {
                        int k = bufferedimage.getRGB(i, j);
                        bufferedimage.setRGB(i, j, (int)((float)(k >> 24 & 0xff) * settings.alpha) << 24 | k & 0xffffff);
                    }
                }

                file.getParentFile().mkdirs();
                ImageIO.write(bufferedimage, "png", file);
            }

            loadTexture(s1, readTextureImage(new BufferedInputStream(new FileInputStream(file))));
            textures.add(s1);
            return s1;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            illegalargumentexception.printStackTrace();
        }
        catch (IllegalAccessException illegalaccessexception)
        {
            illegalaccessexception.printStackTrace();
        }

        return s;
    }

    private int loadTexture(String s, BufferedImage bufferedimage) throws IllegalArgumentException, IllegalAccessException
    {
        HashMap hashmap = (HashMap)fieldTextureMap.get(settings.minecraft.renderEngine);
        IntBuffer intbuffer = (IntBuffer)fieldSingleIntBuffer.get(settings.minecraft.renderEngine);
        Integer integer = (Integer)hashmap.get(s);

        if (integer != null)
        {
            return integer.intValue();
        }

        try
        {
            intbuffer.clear();
            GLAllocation.generateTextureNames(intbuffer);
            int i = intbuffer.get(0);
            settings.minecraft.renderEngine.setupTexture(bufferedimage, i);
            hashmap.put(s, Integer.valueOf(i));
            return i;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        GLAllocation.generateTextureNames(intbuffer);
        int j = intbuffer.get(0);
        settings.minecraft.renderEngine.setupTexture(missingTextureImage, j);
        hashmap.put(s, Integer.valueOf(j));
        return j;
    }

    private BufferedImage readTextureImage(InputStream inputstream) throws IOException
    {
        BufferedImage bufferedimage = ImageIO.read(inputstream);
        inputstream.close();
        return bufferedimage;
    }
}
