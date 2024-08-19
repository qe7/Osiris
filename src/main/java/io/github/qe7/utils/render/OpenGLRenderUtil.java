package io.github.qe7.utils.render;

import io.github.qe7.Osiris;
import io.github.qe7.utils.UtilBase;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * OpenGL Render Utility class for rendering OpenGL elements
 */
public final class OpenGLRenderUtil extends UtilBase {

    // credit to https://www.youtube.com/@billybob1060yt for the following methods, I'm too lazy
    // had to fix some *all* of the methods because they were broken :p - Shae

    /**
     * Draws an outlined bounding box
     *
     * @param aa The AxisAlignedBB to draw
     */
    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
    }

    /**
     * Draws a bounding box
     *
     * @param aa The AxisAlignedBB to draw
     */
    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.addVertex(aa.minX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.maxZ);
        tessellator.addVertex(aa.minX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.minX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.minZ);
        tessellator.addVertex(aa.maxX, aa.maxY, aa.maxZ);
        tessellator.addVertex(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
    }

    /**
     * Draws a solid bounding box
     *
     * @param x         The x position of the bounding box
     * @param y         The y position of the bounding box
     * @param z         The z position of the bounding box
     * @param color     The color of the bounding box
     * @param lineWidth The width of the bounding box outline
     */
    public static void drawBlockESP(double x, double y, double z, Color color, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 0.25f);
        drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 0.8f);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /**
     * Draws an entity ESP
     *
     * @param x         The x position of the entity
     * @param y         The y position of the entity
     * @param z         The z position of the entity
     * @param width     The width of the entity
     * @param height    The height of the entity
     * @param color     The color of the entity
     * @param lineWidth The width of the entity outline
     */
    public static void drawEntityESP(double x, double y, double z, double width, double height, Color color, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 0.25f);
        drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 0.8f);
        drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    /**
     * Draws a tracer line
     *
     * @param x         The x position of the tracer line
     * @param y         The y position of the tracer line
     * @param z         The z position of the tracer line
     * @param color     The color of the tracer line
     * @param lineWidth The width of the tracer line
     */
    public static void drawTracerLine(double x, double y, double z, Color color, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND); // 3042
        GL11.glEnable(GL11.GL_LINE_SMOOTH); // 2848
        GL11.glDisable(GL11.GL_DEPTH_TEST); // 2929
        GL11.glDisable(GL11.GL_TEXTURE_2D); // 3553
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND); // 3042
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 1.0F);
        GL11.glBegin(2);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    /**
     * Converts world coordinates to screen coordinates
     *
     * @param worldX The world x coordinate
     * @param worldY The world y coordinate
     * @param worldZ The world z coordinate
     * @return The screen coordinates
     */
    public static float[] worldToScreen(double worldX, double worldY, double worldZ) {
        FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);

        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        boolean result = GLU.gluProject((float) worldX, (float) worldY, (float) worldZ, modelView, projection, viewport, screenCoords);

        if (result) {
            return new float[]{screenCoords.get(0), screenCoords.get(1), screenCoords.get(2)};
        }
        return null;
    }

    /**
     * Draws a name tag above an entity
     *
     * @param entity The entity to draw the name tag above
     * @param renderManager The render manager
     * @param x The x position of the entity
     * @param y The y position of the entity
     * @param z The z position of the entity
     * @param scale The scale of the name tag
     */
    public static void drawName(final Entity entity, final RenderManager renderManager, final double x, final double y, final double z, float scale) {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final String name = ((EntityPlayer) entity).username;

        final double distance = entity.getDistanceToEntity(mc.thePlayer);

        final float renderScale = 0.01666667F * scale * (distance < 8 ? 1 : (float) distance / 8);

        GL11.glPushMatrix();

        GL11.glTranslated(x, y + 2.5F, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);

        // Rotate to face the player
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        // Scale down
        GL11.glScalef(-renderScale, -renderScale, renderScale);

        // Disable lighting and depth test for rendering
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Render the name
        String displayName = name + " §2" + ((EntityPlayer) entity).getHealth() + " §7[" + Math.round(entity.getDistanceToEntity(mc.thePlayer) * 10) / 10.0 + "m]";
        int width = fontRenderer.getStringWidth(displayName.replaceAll("§.", "")) / 2;

        Gui.drawRect(-width - 2, -1, width + 2, 9, 0x80000000);
        fontRenderer.drawStringWithShadow(displayName, -width, 0, Osiris.getInstance().getRelationManager().isFriend(name) ? new Color(63, 124, 182).getRGB() : Osiris.getInstance().getRelationManager().isEnemy(name) ? new Color(255, 0, 0).getRGB() : -1);

        // Re-enable lighting and depth test
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();
    }
}
