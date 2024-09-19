package lunatrius.schematica;

import java.util.*;
import net.minecraft.src.*;

public class SchematicWorld extends World
{
    private static final SaveHandlerMP saveHandlerMP = new SaveHandlerMP();
    private static final WorldSettings worldSettings;
    private int blocks[][][];
    private int metadata[][][];
    private List tileEntities;
    private short width;
    private short length;
    private short height;

    public SchematicWorld()
    {
        super(saveHandlerMP, "", worldSettings);
        blocks = (int[][][])null;
        metadata = (int[][][])null;
        tileEntities = null;
        width = 0;
        length = 0;
        height = 0;
    }

    public SchematicWorld(int ai[][][], int ai1[][][], List list, short word0, short word1, short word2)
    {
        super(saveHandlerMP, "", worldSettings);
        blocks = ai;
        metadata = ai1;
        tileEntities = list;
        width = word0;
        length = word2;
        height = word1;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        byte abyte0[] = nbttagcompound.getByteArray("Blocks");
        byte abyte1[] = nbttagcompound.getByteArray("Data");
        boolean flag = false;
        byte abyte2[] = null;

        if ((flag = nbttagcompound.hasKey("Add")))
        {
            abyte2 = nbttagcompound.getByteArray("Add");
        }

        width = nbttagcompound.getShort("Width");
        length = nbttagcompound.getShort("Length");
        height = nbttagcompound.getShort("Height");
        blocks = new int[width][height][length];
        metadata = new int[width][height][length];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                for (int l = 0; l < length; l++)
                {
                    blocks[i][j][l] = abyte0[i + (j * length + l) * width] & 0xff;
                    metadata[i][j][l] = abyte1[i + (j * length + l) * width] & 0xff;

                    if (flag)
                    {
                        blocks[i][j][l] |= (abyte2[i + (j * length + l) * width] & 0xff) << 8;
                    }
                }
            }
        }

        tileEntities = new ArrayList();
        NBTTagList nbttaglist = nbttagcompound.getTagList("TileEntities");

        for (int k = 0; k < nbttaglist.tagCount(); k++)
        {
            TileEntity tileentity = TileEntity.createAndLoadEntity((NBTTagCompound)nbttaglist.tagAt(k));
            tileentity.worldObj = this;
            tileEntities.add(tileentity);
        }

        refreshChests();
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Width", width);
        nbttagcompound.setShort("Length", length);
        nbttagcompound.setShort("Height", height);
        byte abyte0[] = new byte[width * length * height];
        byte abyte1[] = new byte[width * length * height];
        byte abyte2[] = new byte[width * length * height];
        boolean flag = false;

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                for (int k = 0; k < length; k++)
                {
                    abyte0[i + (j * length + k) * width] = (byte)blocks[i][j][k];
                    abyte1[i + (j * length + k) * width] = (byte)metadata[i][j][k];

                    if ((abyte2[i + (j * length + k) * width] = (byte)(blocks[i][j][k] >> 8)) > 0)
                    {
                        flag = true;
                    }
                }
            }
        }

        nbttagcompound.setString("Materials", "Classic");
        nbttagcompound.setByteArray("Blocks", abyte0);
        nbttagcompound.setByteArray("Data", abyte1);

        if (flag)
        {
            nbttagcompound.setByteArray("Add", abyte2);
        }

        nbttagcompound.setTag("Entities", new NBTTagList());
        NBTTagList nbttaglist = new NBTTagList();
        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = tileEntities.iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound1))
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            nbttagcompound1 = new NBTTagCompound();
            tileentity.writeToNBT(nbttagcompound1);
        }

        nbttagcompound.setTag("TileEntities", nbttaglist);
    }

    /**
     * Returns the block ID at coords x,y,z
     */
    public int getBlockId(int i, int j, int k)
    {
        if (i < 0 || j < 0 || k < 0 || i >= width || j >= height || k >= length)
        {
            return 0;
        }
        else
        {
            return blocks[i][j][k] & 0xfff;
        }
    }

    /**
     * Returns the TileEntity associated with a given block in X,Y,Z coordinates, or null if no TileEntity exists
     */
    public TileEntity getBlockTileEntity(int i, int j, int k)
    {
        for (int l = 0; l < tileEntities.size(); l++)
        {
            if (((TileEntity)tileEntities.get(l)).xCoord == i && ((TileEntity)tileEntities.get(l)).yCoord == j && ((TileEntity)tileEntities.get(l)).zCoord == k)
            {
                return (TileEntity)tileEntities.get(l);
            }
        }

        return null;
    }

    /**
     * 'Any Light rendered on a 1.8 Block goes through here'
     */
    public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l)
    {
        return 0;
    }

    public float getBrightness(int i, int j, int k, int l)
    {
        return 0.0F;
    }

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    public float getLightBrightness(int i, int j, int k)
    {
        return 0.0F;
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int i, int j, int k)
    {
        if (i < 0 || j < 0 || k < 0 || i >= width || j >= height || k >= length)
        {
            return 0;
        }
        else
        {
            return metadata[i][j][k];
        }
    }

    /**
     * Returns the block's material.
     */
    public Material getBlockMaterial(int i, int j, int k)
    {
        return getBlock(i, j, k) == null ? Material.air : getBlock(i, j, k).blockMaterial;
    }

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    public boolean isBlockOpaqueCube(int i, int j, int k)
    {
        return getBlock(i, j, k) != null && getBlock(i, j, k).isOpaqueCube();
    }

    /**
     * Indicate if a material is a normal solid opaque cube.
     */
    public boolean isBlockNormalCube(int i, int j, int k)
    {
        return getBlockMaterial(i, j, k).isOpaque() && getBlock(i, j, k) != null && getBlock(i, j, k).renderAsNormalBlock();
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int i, int j, int k)
    {
        if (i < 0 || j < 0 || k < 0 || i >= width || j >= height || k >= length)
        {
            return true;
        }
        else
        {
            return blocks[i][j][k] == 0;
        }
    }

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int i, int j)
    {
        return BiomeGenBase.forest;
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return height + 1;
    }

    public boolean func_48452_a()
    {
        return false;
    }

    public void setBlockMetadata(int i, int j, int k, byte byte0)
    {
        metadata[i][j][k] = byte0;
    }

    public Block getBlock(int i, int j, int k)
    {
        return Block.blocksList[getBlockId(i, j, k)];
    }

    public void setTileEntities(List list)
    {
        tileEntities = list;
    }

    public List getTileEntities()
    {
        return tileEntities;
    }

    public void refreshChests()
    {
        for (int i = 0; i < tileEntities.size(); i++)
        {
            TileEntity tileentity = (TileEntity)tileEntities.get(i);

            if (tileentity instanceof TileEntityChest)
            {
                checkForAdjacentChests((TileEntityChest)tileentity);
            }
        }
    }

    private void checkForAdjacentChests(TileEntityChest tileentitychest)
    {
        tileentitychest.adjacentChestChecked = true;
        tileentitychest.adjacentChestZNeg = null;
        tileentitychest.adjacentChestXPos = null;
        tileentitychest.adjacentChestXNeg = null;
        tileentitychest.adjacentChestZPos = null;

        if (getBlockId(tileentitychest.xCoord - 1, tileentitychest.yCoord, tileentitychest.zCoord) == Block.chest.blockID)
        {
            tileentitychest.adjacentChestXNeg = (TileEntityChest)getBlockTileEntity(tileentitychest.xCoord - 1, tileentitychest.yCoord, tileentitychest.zCoord);
        }

        if (getBlockId(tileentitychest.xCoord + 1, tileentitychest.yCoord, tileentitychest.zCoord) == Block.chest.blockID)
        {
            tileentitychest.adjacentChestXPos = (TileEntityChest)getBlockTileEntity(tileentitychest.xCoord + 1, tileentitychest.yCoord, tileentitychest.zCoord);
        }

        if (getBlockId(tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord - 1) == Block.chest.blockID)
        {
            tileentitychest.adjacentChestZNeg = (TileEntityChest)getBlockTileEntity(tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord - 1);
        }

        if (getBlockId(tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord + 1) == Block.chest.blockID)
        {
            tileentitychest.adjacentChestZPos = (TileEntityChest)getBlockTileEntity(tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord + 1);
        }
    }

    public void flip()
    {
        for (int k = 0; k < width; k++)
        {
            for (int l = 0; l < height; l++)
            {
                for (int j1 = 0; j1 < (length + 1) / 2; j1++)
                {
                    int i = blocks[k][l][j1];
                    blocks[k][l][j1] = blocks[k][l][length - 1 - j1];
                    blocks[k][l][length - 1 - j1] = i;

                    if (j1 == length - 1 - j1)
                    {
                        metadata[k][l][j1] = flipMetadataZ(metadata[k][l][j1], blocks[k][l][j1]);
                    }
                    else
                    {
                        int j = metadata[k][l][j1];
                        metadata[k][l][j1] = flipMetadataZ(metadata[k][l][length - 1 - j1], blocks[k][l][j1]);
                        metadata[k][l][length - 1 - j1] = flipMetadataZ(j, blocks[k][l][length - 1 - j1]);
                    }
                }
            }
        }

        for (int i1 = 0; i1 < tileEntities.size(); i1++)
        {
            TileEntity tileentity = (TileEntity)tileEntities.get(i1);
            tileentity.zCoord = length - 1 - tileentity.zCoord;
        }

        refreshChests();
    }

    private int flipMetadataZ(int i, int j)
    {
        if (j == Block.torchWood.blockID || j == Block.torchRedstoneActive.blockID || j == Block.torchRedstoneIdle.blockID)
        {
            switch (i)
            {
                case 3:
                    return 4;

                case 4:
                    return 3;
            }
        }
        else if (j == Block.rail.blockID)
        {
            switch (i)
            {
                case 4:
                    return 5;

                case 5:
                    return 4;

                case 6:
                    return 9;

                case 7:
                    return 8;

                case 8:
                    return 7;

                case 9:
                    return 6;
            }
        }
        else if (j == Block.railDetector.blockID || j == Block.railPowered.blockID)
        {
            switch (i & 7)
            {
                case 4:
                    return (byte)(5 | i & 8);

                case 5:
                    return (byte)(4 | i & 8);
            }
        }
        else if (j == Block.stairCompactCobblestone.blockID || j == Block.stairCompactPlanks.blockID || j == Block.stairsBrick.blockID || j == Block.stairsNetherBrick.blockID || j == Block.stairsStoneBrickSmooth.blockID)
        {
            switch (i & 3)
            {
                case 2:
                    return (byte)(3 | i & 4);

                case 3:
                    return (byte)(2 | i & 4);
            }
        }
        else if (j == Block.lever.blockID)
        {
            switch (i & 7)
            {
                case 3:
                    return (byte)(4 | i & 8);

                case 4:
                    return (byte)(3 | i & 8);
            }
        }
        else if (j == Block.doorWood.blockID || j == Block.doorSteel.blockID)
        {
            if ((i & 8) == 8)
            {
                return (byte)(i ^ 1);
            }

            switch (i & 3)
            {
                case 1:
                    return (byte)(3 | i & 0xc);

                case 3:
                    return (byte)(1 | i & 0xc);
            }
        }
        else if (j == Block.button.blockID)
        {
            switch (i & 7)
            {
                case 3:
                    return (byte)(4 | i & 8);

                case 4:
                    return (byte)(3 | i & 8);
            }
        }
        else if (j == Block.signPost.blockID)
        {
            switch (i)
            {
                case 0:
                    return 8;

                case 1:
                    return 7;

                case 2:
                    return 6;

                case 3:
                    return 5;

                case 4:
                    return 4;

                case 5:
                    return 3;

                case 6:
                    return 2;

                case 7:
                    return 1;

                case 8:
                    return 0;

                case 9:
                    return 15;

                case 10:
                    return 14;

                case 11:
                    return 13;

                case 12:
                    return 12;

                case 13:
                    return 11;

                case 14:
                    return 10;

                case 15:
                    return 9;
            }
        }
        else if (j == Block.ladder.blockID || j == Block.signWall.blockID || j == Block.stoneOvenActive.blockID || j == Block.stoneOvenIdle.blockID || j == Block.dispenser.blockID || j == Block.chest.blockID)
        {
            switch (i)
            {
                case 2:
                    return 3;

                case 3:
                    return 2;
            }
        }
        else if (j == Block.pumpkin.blockID || j == Block.pumpkinLantern.blockID)
        {
            switch (i)
            {
                case 0:
                    return 2;

                case 2:
                    return 0;
            }
        }
        else if (j == Block.bed.blockID)
        {
            switch (i & 3)
            {
                case 0:
                    return (byte)(2 | i & 0xc);

                case 2:
                    return (byte)(0 | i & 0xc);
            }
        }
        else if (j == Block.redstoneRepeaterActive.blockID || j == Block.redstoneRepeaterIdle.blockID)
        {
            switch (i & 3)
            {
                case 0:
                    return (byte)(2 | i & 0xc);

                case 2:
                    return (byte)(0 | i & 0xc);
            }
        }
        else if (j == Block.trapdoor.blockID)
        {
            switch (i)
            {
                case 0:
                    return 1;

                case 1:
                    return 0;
            }
        }
        else if (j == Block.pistonBase.blockID || j == Block.pistonStickyBase.blockID || j == Block.pistonExtension.blockID)
        {
            switch (i & 7)
            {
                case 2:
                    return (byte)(3 | i & 8);

                case 3:
                    return (byte)(2 | i & 8);
            }
        }
        else
        {
            if (j == Block.vine.blockID)
            {
                return (byte)(i & 0xa | (i & 1) << 2 | (i & 4) >> 2);
            }

            if (j == Block.fenceGate.blockID)
            {
                switch (i & 3)
                {
                    case 0:
                        return (byte)(2 | i & 4);

                    case 2:
                        return (byte)(0 | i & 4);
                }
            }
        }

        return i;
    }

    public void rotate()
    {
        int ai[][][] = new int[length][height][width];
        int ai1[][][] = new int[length][height][width];

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                for (int l = 0; l < length; l++)
                {
                    ai[l][j][i] = blocks[width - 1 - i][j][l];
                    ai1[l][j][i] = rotateMetadata(metadata[width - 1 - i][j][l], blocks[width - 1 - i][j][l]);
                }
            }
        }

        blocks = ai;
        metadata = ai1;

        for (int i1 = 0; i1 < tileEntities.size(); i1++)
        {
            TileEntity tileentity = (TileEntity)tileEntities.get(i1);
            int k = tileentity.xCoord;
            tileentity.xCoord = tileentity.zCoord;
            tileentity.zCoord = width - 1 - k;
        }

        refreshChests();
        short word0 = width;
        width = length;
        length = word0;
    }

    private int rotateMetadata(int i, int j)
    {
        if (j == Block.torchWood.blockID || j == Block.torchRedstoneActive.blockID || j == Block.torchRedstoneIdle.blockID)
        {
            switch (i)
            {
                case 1:
                    return 4;

                case 2:
                    return 3;

                case 3:
                    return 1;

                case 4:
                    return 2;
            }
        }
        else if (j == Block.rail.blockID)
        {
            switch (i)
            {
                case 0:
                    return 1;

                case 1:
                    return 0;

                case 2:
                    return 4;

                case 3:
                    return 5;

                case 4:
                    return 3;

                case 5:
                    return 2;

                case 6:
                    return 9;

                case 7:
                    return 6;

                case 8:
                    return 7;

                case 9:
                    return 8;
            }
        }
        else if (j == Block.railDetector.blockID || j == Block.railPowered.blockID)
        {
            switch (i & 7)
            {
                case 0:
                    return (byte)(1 | i & 8);

                case 1:
                    return (byte)(0 | i & 8);

                case 2:
                    return (byte)(4 | i & 8);

                case 3:
                    return (byte)(5 | i & 8);

                case 4:
                    return (byte)(3 | i & 8);

                case 5:
                    return (byte)(2 | i & 8);
            }
        }
        else if (j == Block.stairCompactCobblestone.blockID || j == Block.stairCompactPlanks.blockID || j == Block.stairsBrick.blockID || j == Block.stairsNetherBrick.blockID || j == Block.stairsStoneBrickSmooth.blockID)
        {
            switch (i & 3)
            {
                case 0:
                    return (byte)(3 | i & 4);

                case 1:
                    return (byte)(2 | i & 4);

                case 2:
                    return (byte)(0 | i & 4);

                case 3:
                    return (byte)(1 | i & 4);
            }
        }
        else if (j == Block.lever.blockID)
        {
            switch (i & 7)
            {
                case 1:
                    return (byte)(4 | i & 8);

                case 2:
                    return (byte)(3 | i & 8);

                case 3:
                    return (byte)(1 | i & 8);

                case 4:
                    return (byte)(2 | i & 8);

                case 5:
                    return (byte)(6 | i & 8);

                case 6:
                    return (byte)(5 | i & 8);
            }
        }
        else if (j == Block.doorWood.blockID || j == Block.doorSteel.blockID)
        {
            if ((i & 8) == 8)
            {
                return i;
            }

            switch (i & 3)
            {
                case 0:
                    return (byte)(3 | i & 0xc);

                case 1:
                    return (byte)(0 | i & 0xc);

                case 2:
                    return (byte)(1 | i & 0xc);

                case 3:
                    return (byte)(2 | i & 0xc);
            }
        }
        else if (j == Block.button.blockID)
        {
            switch (i & 7)
            {
                case 1:
                    return (byte)(4 | i & 8);

                case 2:
                    return (byte)(3 | i & 8);

                case 3:
                    return (byte)(1 | i & 8);

                case 4:
                    return (byte)(2 | i & 8);
            }
        }
        else
        {
            if (j == Block.signPost.blockID)
            {
                return (byte)((i + 12) % 16);
            }

            if (j == Block.ladder.blockID || j == Block.signWall.blockID || j == Block.stoneOvenActive.blockID || j == Block.stoneOvenIdle.blockID || j == Block.dispenser.blockID || j == Block.chest.blockID)
            {
                switch (i)
                {
                    case 2:
                        return 4;

                    case 3:
                        return 5;

                    case 4:
                        return 3;

                    case 5:
                        return 2;
                }
            }
            else if (j == Block.pumpkin.blockID || j == Block.pumpkinLantern.blockID)
            {
                switch (i)
                {
                    case 0:
                        return 3;

                    case 1:
                        return 0;

                    case 2:
                        return 1;

                    case 3:
                        return 2;
                }
            }
            else if (j == Block.bed.blockID)
            {
                switch (i & 3)
                {
                    case 0:
                        return (byte)(3 | i & 0xc);

                    case 1:
                        return (byte)(0 | i & 0xc);

                    case 2:
                        return (byte)(1 | i & 0xc);

                    case 3:
                        return (byte)(2 | i & 0xc);
                }
            }
            else if (j == Block.redstoneRepeaterActive.blockID || j == Block.redstoneRepeaterIdle.blockID)
            {
                switch (i & 3)
                {
                    case 0:
                        return (byte)(3 | i & 0xc);

                    case 1:
                        return (byte)(0 | i & 0xc);

                    case 2:
                        return (byte)(1 | i & 0xc);

                    case 3:
                        return (byte)(2 | i & 0xc);
                }
            }
            else if (j == Block.trapdoor.blockID)
            {
                switch (i)
                {
                    case 0:
                        return 2;

                    case 1:
                        return 3;

                    case 2:
                        return 1;

                    case 3:
                        return 0;
                }
            }
            else if (j == Block.pistonBase.blockID || j == Block.pistonStickyBase.blockID || j == Block.pistonExtension.blockID)
            {
                switch (i & 7)
                {
                    case 0:
                        return (byte)(0 | i & 8);

                    case 1:
                        return (byte)(1 | i & 8);

                    case 2:
                        return (byte)(4 | i & 8);

                    case 3:
                        return (byte)(5 | i & 8);

                    case 4:
                        return (byte)(3 | i & 8);

                    case 5:
                        return (byte)(2 | i & 8);
                }
            }
            else
            {
                if (j == Block.vine.blockID)
                {
                    return (byte)(i >> 1 | (i & 1) << 3);
                }

                if (j == Block.fenceGate.blockID)
                {
                    switch (i & 3)
                    {
                        case 0:
                            return (byte)(3 | i & 4);

                        case 1:
                            return (byte)(0 | i & 4);

                        case 2:
                            return (byte)(1 | i & 4);

                        case 3:
                            return (byte)(2 | i & 4);
                    }
                }
            }
        }

        return i;
    }

    public int width()
    {
        return width;
    }

    public int length()
    {
        return length;
    }

    public int height()
    {
        return height;
    }

    static
    {
        worldSettings = new WorldSettings(0L, 0, false, false, WorldType.FLAT);
    }
}
