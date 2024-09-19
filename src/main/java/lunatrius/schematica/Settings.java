package lunatrius.schematica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lunatrius.utils.Vector3f;
import lunatrius.utils.Vector3i;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class Settings
{
    private static final Settings instance = new Settings();
    public boolean enableAlpha;
    public float alpha;
    public boolean highlight;
    public float blockDelta;
    public Vector3i renderRange;
    public KeyBinding keyBindings[] =
    {
        new KeyBinding("Schematica Load", 181), new KeyBinding("Schematica Save", 55), new KeyBinding("Schematica Control", 74)
    };
    public static final File schematicDirectory = new File(Minecraft.getMinecraftDir(), "/schematics/");
    public static final File textureDirectory = new File(Minecraft.getMinecraftDir(), "/resources/mod/schematica/");
    public Minecraft minecraft;
    public SchematicWorld schematic;
    public Vector3f playerPosition;
    public RenderBlocks renderBlocks;
    public RenderTileEntity renderTileEntity;
    public int selectedSchematic;
    public Vector3i pointA;
    public Vector3i pointB;
    public Vector3i pointMin;
    public Vector3i pointMax;
    public int rotationRender;
    public Vector3i offset;
    public boolean isRenderingSchematic;
    public int renderingLayer;
    public boolean isRenderingGuide;
    public int increments[] =
    {
        1, 5, 15, 50, 250
    };

    private Settings()
    {
        enableAlpha = false;
        alpha = 1.0F;
        highlight = true;
        blockDelta = 0.005F;
        renderRange = new Vector3i(20, 15, 20);
        minecraft = Minecraft.getMinecraft();
        schematic = null;
        playerPosition = new Vector3f();
        renderBlocks = null;
        renderTileEntity = null;
        selectedSchematic = 0;
        pointA = new Vector3i();
        pointB = new Vector3i();
        pointMin = new Vector3i();
        pointMax = new Vector3i();
        rotationRender = 0;
        offset = new Vector3i();
        isRenderingSchematic = false;
        renderingLayer = -1;
        isRenderingGuide = false;
    }

    public static Settings instance()
    {
        return instance;
    }

    public void keyboardEvent(KeyBinding keybinding)
    {
        if (minecraft.currentScreen == null)
        {
            int i = 0;

            do
            {
                if (i >= keyBindings.length)
                {
                    break;
                }

                if (keybinding == keyBindings[i])
                {
                    keyboardEvent(i);
                    break;
                }

                i++;
            }
            while (true);
        }
    }
    public void keyboardEvent_kc(int keycode)
    {
        if (minecraft.currentScreen == null)
        {
            int i = 0;

            do
            {
                if (i >= keyBindings.length)
                {
                    break;
                }

                if (keycode == keyBindings[i].keyCode)
                {
                    keyboardEvent(i);
                    break;
                }

                i++;
            }
            while (true);
        }
    }
    public void keyboardEvent(int i)
    {
        switch (i)
        {
            case 0:
                minecraft.displayGuiScreen(new GuiSchematicLoad(minecraft.currentScreen));
                break;

            case 1:
                minecraft.displayGuiScreen(new GuiSchematicSave(minecraft.currentScreen));
                break;

            case 2:
                minecraft.displayGuiScreen(new GuiSchematicControl(minecraft.currentScreen));
                break;
        }
    }

    public List getSchematicFiles()
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add("-- No schematic --");
        File afile[] = schematicDirectory.listFiles(new FileFilterSchematic());

        for (int i = 0; i < afile.length; i++)
        {
            arraylist.add(afile[i].getName());
        }

        return arraylist;
    }

    public boolean loadSchematic(String s)
    {
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s);
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);

            if (nbttagcompound != null)
            {
                schematic = new SchematicWorld();
                schematic.readFromNBT(nbttagcompound);
                renderBlocks = new RenderBlocks(schematic);
                renderTileEntity = new RenderTileEntity(schematic);
                isRenderingSchematic = true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            schematic = null;
            renderBlocks = null;
            renderTileEntity = null;
            isRenderingSchematic = false;
            return false;
        }

        return true;
    }

    public boolean saveSchematic(String s, Vector3i vector3i, Vector3i vector3i1)
    {
        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            int i = Math.min(vector3i.x, vector3i1.x);
            int j = Math.max(vector3i.x, vector3i1.x);
            int k = Math.min(vector3i.y, vector3i1.y);
            int l = Math.max(vector3i.y, vector3i1.y);
            int i1 = Math.min(vector3i.z, vector3i1.z);
            int j1 = Math.max(vector3i.z, vector3i1.z);
            short word0 = (short)(Math.abs(j - i) + 1);
            short word1 = (short)(Math.abs(l - k) + 1);
            short word2 = (short)(Math.abs(j1 - i1) + 1);
            int ai[][][] = new int[word0][word1][word2];
            int ai1[][][] = new int[word0][word1][word2];
            ArrayList arraylist = new ArrayList();

            for (int k1 = i; k1 <= j; k1++)
            {
                for (int l1 = k; l1 <= l; l1++)
                {
                    for (int i2 = i1; i2 <= j1; i2++)
                    {
                        ai[k1 - i][l1 - k][i2 - i1] = minecraft.theWorld.getBlockId(k1, l1, i2);
                        ai1[k1 - i][l1 - k][i2 - i1] = minecraft.theWorld.getBlockMetadata(k1, l1, i2);

                        if (minecraft.theWorld.getBlockTileEntity(k1, l1, i2) != null)
                        {
                            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                            minecraft.theWorld.getBlockTileEntity(k1, l1, i2).writeToNBT(nbttagcompound1);
                            TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound1);
                            tileentity.xCoord -= i;
                            tileentity.yCoord -= k;
                            tileentity.zCoord -= i1;
                            arraylist.add(tileentity);
                        }
                    }
                }
            }

            SchematicWorld schematicworld = new SchematicWorld(ai, ai1, arraylist, word0, word1, word2);
            schematicworld.writeToNBT(nbttagcompound);
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public float getTranslationX()
    {
        return playerPosition.x - (float)offset.x;
    }

    public float getTranslationY()
    {
        return playerPosition.y - (float)offset.y;
    }

    public float getTranslationZ()
    {
        return playerPosition.z - (float)offset.z;
    }

    public void updatePoints()
    {
        pointMin.x = Math.min(pointA.x, pointB.x);
        pointMin.y = Math.min(pointA.y, pointB.y);
        pointMin.z = Math.min(pointA.z, pointB.z);
        pointMax.x = Math.max(pointA.x, pointB.x);
        pointMax.y = Math.max(pointA.y, pointB.y);
        pointMax.z = Math.max(pointA.z, pointB.z);
    }

    public void moveHere(Vector3i vector3i)
    {
        vector3i.x = (int)Math.floor(playerPosition.x);
        vector3i.y = (int)Math.floor(playerPosition.y - 1.0F);
        vector3i.z = (int)Math.floor(playerPosition.z);

        switch (rotationRender)
        {
            case 0:
                vector3i.x--;
                vector3i.z++;
                break;

            case 1:
                vector3i.x--;
                vector3i.z--;
                break;

            case 2:
                vector3i.x++;
                vector3i.z--;
                break;

            case 3:
                vector3i.x++;
                vector3i.z++;
                break;
        }
    }

    public void moveHere()
    {
        offset.x = (int)Math.floor(playerPosition.x);
        offset.y = (int)Math.floor(playerPosition.y) - 1;
        offset.z = (int)Math.floor(playerPosition.z);

        if (schematic != null)
        {
            switch (rotationRender)
            {
                case 0:
                    offset.x -= schematic.width();
                    offset.z++;
                    break;

                case 1:
                    offset.x -= schematic.width();
                    offset.z -= schematic.length();
                    break;

                case 2:
                    offset.x++;
                    offset.z -= schematic.length();
                    break;

                case 3:
                    offset.x++;
                    offset.z++;
                    break;
            }
        }
    }

    public void toggleRendering()
    {
        isRenderingSchematic = !isRenderingSchematic && schematic != null;
    }

    public void flipWorld()
    {
        if (schematic != null)
        {
            schematic.flip();
        }
    }

    public void rotateWorld()
    {
        if (schematic != null)
        {
            schematic.rotate();
        }
    }
}
