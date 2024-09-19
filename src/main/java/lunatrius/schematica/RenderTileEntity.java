package lunatrius.schematica;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTileEntity
{
    private final Settings settings = Settings.instance();
    private final ModelChest chestModel = new ModelChest();
    private final ModelChest largeChestModel = new ModelLargeChest();
    private final ModelSign modelSign = new ModelSign();
    private final SchematicWorld world;

    public RenderTileEntity(SchematicWorld schematicworld)
    {
        world = schematicworld;
    }

    public void renderTileEntityChestAt(TileEntityChest tileentitychest)
    {
        int i = 0;
        Block block = getBlockType(tileentitychest);

        if (block != null)
        {
            unifyAdjacentChests((BlockChest)block, world, tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord);
            i = getBlockMetadata(tileentitychest);
        }

        if (tileentitychest.adjacentChestZNeg == null && tileentitychest.adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (tileentitychest.adjacentChestXPos == null && tileentitychest.adjacentChestZPos == null)
            {
                modelchest = chestModel;
                bindTextureByName("/item/chest.png");
            }
            else
            {
                modelchest = largeChestModel;
                bindTextureByName("/item/largechest.png");
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(tileentitychest.xCoord, (float)tileentitychest.yCoord + 1.0F, (float)tileentitychest.zCoord + 1.0F);
            GL11.glScalef(1.0F, -1F, -1F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            int j = 0;

            switch (i)
            {
                case 2:
                    j = 180;
                    break;

                case 3:
                    j = 0;
                    break;

                case 4:
                    j = 90;
                    break;

                case 5:
                    j = -90;
                    break;
            }

            if (i == 2 && tileentitychest.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && tileentitychest.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1F);
            }

            GL11.glRotatef(j, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f = tileentitychest.prevLidAngle;

            if (tileentitychest.adjacentChestZNeg != null)
            {
                float f1 = tileentitychest.adjacentChestZNeg.prevLidAngle;

                if (f1 > f)
                {
                    f = f1;
                }
            }

            if (tileentitychest.adjacentChestXNeg != null)
            {
                float f2 = tileentitychest.adjacentChestXNeg.prevLidAngle;

                if (f2 > f)
                {
                    f = f2;
                }
            }

            f = 1.0F - f;
            f = 1.0F - f * f * f;
            modelchest.chestLid.rotateAngleX = -((f * (float)Math.PI) / 2.0F);
            modelchest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        }
    }

    public void renderTileEntitySignAt(TileEntitySign tileentitysign)
    {
        Block block = getBlockType(tileentitysign);
        GL11.glPushMatrix();
        float f = 0.6666667F;

        if (block == Block.signPost)
        {
            GL11.glTranslatef((float)tileentitysign.xCoord + 0.5F, (float)tileentitysign.yCoord + 0.75F * f, (float)tileentitysign.zCoord + 0.5F);
            float f3 = (float)(getBlockMetadata(tileentitysign) * 360) / 16F;
            GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
            modelSign.signStick.showModel = true;
        }
        else
        {
            int i = getBlockMetadata(tileentitysign);
            float f1 = 0.0F;

            if (i == 2)
            {
                f1 = 180F;
            }

            if (i == 4)
            {
                f1 = 90F;
            }

            if (i == 5)
            {
                f1 = -90F;
            }

            GL11.glTranslatef((float)tileentitysign.xCoord + 0.5F, (float)tileentitysign.yCoord + 0.75F * f, (float)tileentitysign.zCoord + 0.5F);
            GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
            modelSign.signStick.showModel = false;
        }

        bindTextureByName("/item/sign.png");
        GL11.glPushMatrix();
        GL11.glScalef(f, -f, -f);
        modelSign.renderSign();
        GL11.glPopMatrix();
        FontRenderer fontrenderer = settings.minecraft.fontRenderer;
        float f2 = 0.01666667F * f;
        GL11.glTranslatef(0.0F, 0.5F * f, 0.07F * f);
        GL11.glScalef(f2, -f2, f2);
        GL11.glNormal3f(0.0F, 0.0F, -1F * f2);
        GL11.glDepthMask(false);
        int j = (int)(settings.alpha * 255F) * 0x1000000;

        for (int k = 0; k < tileentitysign.signText.length; k++)
        {
            String s = tileentitysign.signText[k];
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, k * 10 - tileentitysign.signText.length * 5, j);
        }

        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, settings.alpha);
        GL11.glPopMatrix();
    }

    private Block getBlockType(TileEntity tileentity)
    {
        return world.getBlock(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
    }

    private int getBlockMetadata(TileEntity tileentity)
    {
        return world.getBlockMetadata(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
    }

    private void unifyAdjacentChests(BlockChest blockchest, SchematicWorld schematicworld, int i, int j, int k)
    {
        int l = schematicworld.getBlockId(i, j, k - 1);
        int i1 = schematicworld.getBlockId(i, j, k + 1);
        int j1 = schematicworld.getBlockId(i - 1, j, k);
        int k1 = schematicworld.getBlockId(i + 1, j, k);

        if (l != blockchest.blockID && i1 != blockchest.blockID)
        {
            if (j1 != blockchest.blockID && k1 != blockchest.blockID)
            {
                byte byte0 = 3;

                if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
                {
                    byte0 = 3;
                }

                if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
                {
                    byte0 = 2;
                }

                if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
                {
                    byte0 = 5;
                }

                if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
                {
                    byte0 = 4;
                }
            }
            else
            {
                int l1 = schematicworld.getBlockId(j1 != blockchest.blockID ? i + 1 : i - 1, j, k - 1);
                int j2 = schematicworld.getBlockId(j1 != blockchest.blockID ? i + 1 : i - 1, j, k + 1);
                byte byte1 = 3;
                int l2;

                if (j1 == blockchest.blockID)
                {
                    l2 = schematicworld.getBlockMetadata(i - 1, j, k);
                }
                else
                {
                    l2 = schematicworld.getBlockMetadata(i + 1, j, k);
                }

                if (l2 == 2)
                {
                    byte1 = 2;
                }

                if ((Block.opaqueCubeLookup[l] || Block.opaqueCubeLookup[l1]) && !Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[j2])
                {
                    byte1 = 3;
                }

                if ((Block.opaqueCubeLookup[i1] || Block.opaqueCubeLookup[j2]) && !Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[l1])
                {
                    byte1 = 2;
                }
            }
        }
        else
        {
            int i2 = schematicworld.getBlockId(i - 1, j, l != blockchest.blockID ? k + 1 : k - 1);
            int k2 = schematicworld.getBlockId(i + 1, j, l != blockchest.blockID ? k + 1 : k - 1);
            byte byte2 = 5;
            int i3;

            if (l == blockchest.blockID)
            {
                i3 = schematicworld.getBlockMetadata(i, j, k - 1);
            }
            else
            {
                i3 = schematicworld.getBlockMetadata(i, j, k + 1);
            }

            if (i3 == 4)
            {
                byte2 = 4;
            }

            if ((Block.opaqueCubeLookup[j1] || Block.opaqueCubeLookup[i2]) && !Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[k2])
            {
                byte2 = 5;
            }

            if ((Block.opaqueCubeLookup[k1] || Block.opaqueCubeLookup[k2]) && !Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[i2])
            {
                byte2 = 4;
            }

            schematicworld.setBlockMetadata(i, j, k, byte2);
        }
    }

    private void bindTextureByName(String s)
    {
        RenderEngine renderengine = TileEntityRenderer.instance.renderEngine;

        if (renderengine != null)
        {
            renderengine.bindTexture(renderengine.getTexture(s));
        }
    }
}
