package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChunkProviderClient implements IChunkProvider
{
    /**
     * The completely empty chunk used by ChunkProviderClient when chunkMapping doesn't contain the requested
     * coordinates.
     */
    private Chunk blankChunk;

    /**
     * The mapping between ChunkCoordinates and Chunks that ChunkProviderClient maintains.
     */
    private LongHashMap chunkMapping;
    private List field_889_c;

    /** Reference to the World object. */
    private World worldObj;

    public ChunkProviderClient(World par1World)
    {
        chunkMapping = new LongHashMap();
        field_889_c = new ArrayList();
        blankChunk = new EmptyChunk(par1World, 0, 0);
        worldObj = par1World;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }
    
    public void importOldTileEntities() {
        for (Long obj : this.chunkMapping.keySet()) {
            Chunk chunk = (Chunk) this.chunkMapping.getValueByKey(obj);
            if (chunk != null && chunk.isFilled) {
                chunk.importOldChunkTileEntities();
            }
        }
    }

    public void func_539_c(int par1, int par2)
    {
        Chunk chunk = provideChunk(par1, par2);

        if (!chunk.isEmpty())
        {
            chunk.onChunkUnload();
        }

        if (((WorldClient) this.worldObj).downloadThisWorld && !chunk.neverSave && chunk.isFilled) {
            saveChunk(chunk);
            try {
                ((WorldClient) this.worldObj).downloadChunkLoader.saveExtraChunkData(this.worldObj, chunk);
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
        field_889_c.remove(chunk);
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int par1, int par2)
    {
        Chunk chunk = new Chunk(worldObj, par1, par2);
        chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(par1, par2), chunk);
        chunk.isChunkLoaded = true;
        return chunk;
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int par1, int par2)
    {
        Chunk chunk = (Chunk)chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(par1, par2));

        if (chunk == null)
        {
            return blankChunk;
        }
        else
        {
            return chunk;
        }
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        if (!((WorldClient) this.worldObj).downloadThisWorld) {
            return true;
        }
        for (long obj : this.chunkMapping.keySet()) {
            Chunk chunk = (Chunk) this.chunkMapping.getValueByKey(obj);
            if (flag && chunk != null && !chunk.neverSave && chunk.isFilled) {
                try {
                    ((WorldClient) this.worldObj).downloadChunkLoader.saveExtraChunkData(this.worldObj, chunk);
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
            if (chunk != null && !chunk.neverSave && chunk.isFilled) {
                saveChunk(chunk);
            }
        }
        if (flag) {
            ((WorldClient) this.worldObj).downloadChunkLoader.saveExtraData();
            return true;
        }
        return true;
    }

    private void saveChunk(Chunk chunk) {
        if (!((WorldClient) this.worldObj).downloadThisWorld) {
            return;
        }
        chunk.lastSaveTime = this.worldObj.getWorldTime();
        chunk.isTerrainPopulated = true;
        try {
            for (Object obj : chunk.newChunkTileEntityMap.keySet()) {
                TileEntity tileentity = (TileEntity) chunk.newChunkTileEntityMap.get(obj);
                if (tileentity != null) {
                    Block block = Block.blocksList[this.worldObj.getBlockId(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)];
                    if ((block instanceof BlockChest) || (block instanceof BlockDispenser) || (block instanceof BlockFurnace) || (block instanceof BlockNote)) {
                        chunk.chunkTileEntityMap.put(obj, tileentity);
                    }
                }
            }
            ((WorldClient) this.worldObj).downloadChunkLoader.saveChunk(this.worldObj, chunk);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    /**
     * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list
     * is always empty and will not remove any chunks.
     */
    public boolean unload100OldestChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return false;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return (new StringBuilder()).append("MultiplayerChunkCache: ").append(chunkMapping.getNumHashElements()).toString();
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int i)
    {
        return null;
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int i, int j)
    {
        return null;
    }
}
