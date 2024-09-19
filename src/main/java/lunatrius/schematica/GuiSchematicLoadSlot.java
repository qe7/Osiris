package lunatrius.schematica;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

public class GuiSchematicLoadSlot extends GuiSlot
{
    private final Settings settings = Settings.instance();
    final GuiSchematicLoad parentSchematicGuiChooser;

    public GuiSchematicLoadSlot(GuiSchematicLoad guischematicload)
    {
        super(Settings.instance().minecraft, guischematicload.width, guischematicload.height, 32, (guischematicload.height - 55) + 4, 36);
        parentSchematicGuiChooser = guischematicload;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return settings.getSchematicFiles().size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int i, boolean flag)
    {
        settings.selectedSchematic = i;
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int i)
    {
        return i == settings.selectedSchematic;
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return getSize() * 36;
    }

    protected void drawBackground()
    {
        parentSchematicGuiChooser.drawDefaultBackground();
    }
    public static void bindTexture(String name) {
		int n = Minecraft.getMinecraft().renderEngine.getTexture(name);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,n);
	}
    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
    {
        String s = ((String)settings.getSchematicFiles().get(i)).replaceAll("(?i)\\.schematic$", "");
        bindTexture("/gui/unknown_pack.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xffffff);
        tessellator.addVertexWithUV(j, k + l, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(j + 32, k + l, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(j + 32, k, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(j, k, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        parentSchematicGuiChooser.drawString(settings.minecraft.fontRenderer, s, j + 32 + 2, k + 1, 0xffffff);
    }
}
