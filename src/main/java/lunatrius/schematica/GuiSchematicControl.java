package lunatrius.schematica;

import java.util.List;
import lunatrius.utils.Vector3i;
import net.minecraft.src.*;

public class GuiSchematicControl extends GuiScreen
{
    private final Settings settings = Settings.instance();
    private final GuiScreen prevGuiScreen;
    private int centerX;
    private int centerY;
    private GuiButton btnDecX;
    private GuiButton btnAmountX;
    private GuiButton btnIncX;
    private GuiButton btnDecY;
    private GuiButton btnAmountY;
    private GuiButton btnIncY;
    private GuiButton btnDecZ;
    private GuiButton btnAmountZ;
    private GuiButton btnIncZ;
    private GuiButton btnDecLayer;
    private GuiButton btnIncLayer;
    private GuiButton btnHide;
    private GuiButton btnMove;
    private GuiButton btnFlip;
    private GuiButton btnRotate;
    private int incrementX;
    private int incrementY;
    private int incrementZ;

    public GuiSchematicControl(GuiScreen guiscreen)
    {
        centerX = 0;
        centerY = 0;
        btnDecX = null;
        btnAmountX = null;
        btnIncX = null;
        btnDecY = null;
        btnAmountY = null;
        btnIncY = null;
        btnDecZ = null;
        btnAmountZ = null;
        btnIncZ = null;
        btnDecLayer = null;
        btnIncLayer = null;
        btnHide = null;
        btnMove = null;
        btnFlip = null;
        btnRotate = null;
        incrementX = 0;
        incrementY = 0;
        incrementZ = 0;
        prevGuiScreen = guiscreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        centerX = width / 2;
        centerY = height / 2;
        controlList.clear();
        int i = 0;
        btnDecX = new GuiButton(i++, centerX - 50, centerY - 30, 30, 20, "-");
        controlList.add(btnDecX);
        btnAmountX = new GuiButton(i++, centerX - 15, centerY - 30, 30, 20, Integer.toString(settings.increments[incrementX]));
        controlList.add(btnAmountX);
        btnIncX = new GuiButton(i++, centerX + 20, centerY - 30, 30, 20, "+");
        controlList.add(btnIncX);
        btnDecY = new GuiButton(i++, centerX - 50, centerY - 5, 30, 20, "-");
        controlList.add(btnDecY);
        btnAmountY = new GuiButton(i++, centerX - 15, centerY - 5, 30, 20, Integer.toString(settings.increments[incrementY]));
        controlList.add(btnAmountY);
        btnIncY = new GuiButton(i++, centerX + 20, centerY - 5, 30, 20, "+");
        controlList.add(btnIncY);
        btnDecZ = new GuiButton(i++, centerX - 50, centerY + 20, 30, 20, "-");
        controlList.add(btnDecZ);
        btnAmountZ = new GuiButton(i++, centerX - 15, centerY + 20, 30, 20, Integer.toString(settings.increments[incrementZ]));
        controlList.add(btnAmountZ);
        btnIncZ = new GuiButton(i++, centerX + 20, centerY + 20, 30, 20, "+");
        controlList.add(btnIncZ);
        btnDecLayer = new GuiButton(i++, width - 90, height - 150, 25, 20, "-");
        controlList.add(btnDecLayer);
        btnIncLayer = new GuiButton(i++, width - 35, height - 150, 25, 20, "+");
        controlList.add(btnIncLayer);
        btnHide = new GuiButton(i++, width - 90, height - 105, 80, 20, settings.isRenderingSchematic ? "Hide" : "Show");
        controlList.add(btnHide);
        btnMove = new GuiButton(i++, width - 90, height - 80, 80, 20, "Move here");
        controlList.add(btnMove);
        btnFlip = new GuiButton(i++, width - 90, height - 55, 80, 20, "Flip");
        controlList.add(btnFlip);
        btnRotate = new GuiButton(i++, width - 90, height - 30, 80, 20, "Rotate");
        controlList.add(btnRotate);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id == btnDecX.id)
            {
                settings.offset.x -= settings.increments[incrementX];
            }
            else if (guibutton.id == btnIncX.id)
            {
                settings.offset.x += settings.increments[incrementX];
            }
            else if (guibutton.id == btnAmountX.id)
            {
                incrementX = (incrementX + 1) % settings.increments.length;
                btnAmountX.displayString = Integer.toString(settings.increments[incrementX]);
            }
            else if (guibutton.id == btnDecY.id)
            {
                settings.offset.y -= settings.increments[incrementY];
            }
            else if (guibutton.id == btnIncY.id)
            {
                settings.offset.y += settings.increments[incrementY];
            }
            else if (guibutton.id == btnAmountY.id)
            {
                incrementY = (incrementY + 1) % settings.increments.length;
                btnAmountY.displayString = Integer.toString(settings.increments[incrementY]);
            }
            else if (guibutton.id == btnDecZ.id)
            {
                settings.offset.z -= settings.increments[incrementZ];
            }
            else if (guibutton.id == btnIncZ.id)
            {
                settings.offset.z += settings.increments[incrementZ];
            }
            else if (guibutton.id == btnAmountZ.id)
            {
                incrementZ = (incrementZ + 1) % settings.increments.length;
                btnAmountZ.displayString = Integer.toString(settings.increments[incrementZ]);
            }
            else if (guibutton.id == btnDecLayer.id)
            {
                if (settings.schematic != null)
                {
                    settings.renderingLayer = MathHelper.clamp_int(settings.renderingLayer - 1, -1, settings.schematic.height() - 1);
                }
                else
                {
                    settings.renderingLayer = -1;
                }
            }
            else if (guibutton.id == btnIncLayer.id)
            {
                if (settings.schematic != null)
                {
                    settings.renderingLayer = MathHelper.clamp_int(settings.renderingLayer + 1, -1, settings.schematic.height() - 1);
                }
                else
                {
                    settings.renderingLayer = -1;
                }
            }
            else if (guibutton.id == btnHide.id)
            {
                settings.toggleRendering();
                btnHide.displayString = settings.isRenderingSchematic ? "Hide" : "Show";
            }
            else if (guibutton.id == btnMove.id)
            {
                settings.moveHere();
            }
            else if (guibutton.id == btnFlip.id)
            {
                settings.flipWorld();
            }
            else if (guibutton.id == btnRotate.id)
            {
                settings.rotateWorld();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Move schematic", centerX, centerY - 45, 0xffffff);
        drawCenteredString(fontRenderer, "Layers", width - 50, height - 165, 0xffffff);
        drawCenteredString(fontRenderer, "Operations", width - 50, height - 120, 0xffffff);
        drawCenteredString(fontRenderer, settings.renderingLayer >= 0 ? Integer.toString(settings.renderingLayer + 1) : "ALL", width - 50, height - 145, 0xffffff);
        drawString(fontRenderer, "X", centerX - 65, centerY - 24, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.offset.x), centerX + 55, centerY - 24, 0xffffff);
        drawString(fontRenderer, "Y", centerX - 65, centerY + 1, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.offset.y), centerX + 55, centerY + 1, 0xffffff);
        drawString(fontRenderer, "Z", centerX - 65, centerY + 26, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.offset.z), centerX + 55, centerY + 26, 0xffffff);
        super.drawScreen(i, j, f);
    }
}
