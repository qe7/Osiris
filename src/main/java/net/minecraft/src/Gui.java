package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class Gui {

    protected float zLevel;

    public Gui() {
        zLevel = 0.0F;
    }

    protected void drawHorizontalLine(int par1, int par2, int par3, int par4) {
        if (par2 < par1) {
            int i = par1;
            par1 = par2;
            par2 = i;
        }

        drawRect(par1, par3, par2 + 1, par3 + 1, par4);
    }

    protected void drawVerticalLine(int par1, int par2, int par3, int par4) {
        if (par3 < par2) {
            int i = par2;
            par2 = par3;
            par3 = i;
        }

        drawRect(par1, par2 + 1, par1 + 1, par3, par4);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    public static void drawRect(float par1, float par2, float par3, float par4, int par5) {
        if (par1 < par3) {
            float i = par1;
            par1 = par3;
            par3 = i;
        }

        if (par2 < par4) {
            float j = par2;
            par2 = par4;
            par4 = j;
        }

        float f = (float) (par5 >> 24 & 0xff) / 255F;
        float f1 = (float) (par5 >> 16 & 0xff) / 255F;
        float f2 = (float) (par5 >> 8 & 0xff) / 255F;
        float f3 = (float) (par5 & 0xff) / 255F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(par1, par4, 0.0D);
        tessellator.addVertex(par3, par4, 0.0D);
        tessellator.addVertex(par3, par2, 0.0D);
        tessellator.addVertex(par1, par2, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(float i, float j, float k, float l, int i1, int j1) {
        float f = (float) (i1 >> 24 & 0xff) / 255F;
        float f1 = (float) (i1 >> 16 & 0xff) / 255F;
        float f2 = (float) (i1 >> 8 & 0xff) / 255F;
        float f3 = (float) (i1 & 0xff) / 255F;
        float f4 = (float) (j1 >> 24 & 0xff) / 255F;
        float f5 = (float) (j1 >> 16 & 0xff) / 255F;
        float f6 = (float) (j1 >> 8 & 0xff) / 255F;
        float f7 = (float) (j1 & 0xff) / 255F;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425 /*GL_SMOOTH*/);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(k, j, 0.0D);
        tessellator.addVertex(i, j, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(i, l, 0.0D);
        tessellator.addVertex(k, l, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(7424 /*GL_FLAT*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawCenteredString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
        par1FontRenderer.drawStringWithShadow(par2Str, par3 - par1FontRenderer.getStringWidth(par2Str) / 2, par4, par5);
    }

    /**
     * Renders the specified text to the screen.
     */
    public void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
        par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (float) (par3 + 0) * f, (float) (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (float) (par3 + par5) * f, (float) (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (float) (par3 + par5) * f, (float) (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (float) (par3 + 0) * f, (float) (par4 + 0) * f1);
        tessellator.draw();
    }
}
