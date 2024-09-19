package lunatrius.schematica;

import java.io.File;
import java.util.List;
import lunatrius.utils.Vector3i;
import net.minecraft.src.*;

public class GuiSchematicSave extends GuiScreen
{
    private final Settings settings = Settings.instance();
    private final GuiScreen prevGuiScreen;
    private int centerX;
    private int centerY;
    private GuiButton btnPointA;
    private GuiButton btnDecAX;
    private GuiButton btnAmountAX;
    private GuiButton btnIncAX;
    private GuiButton btnDecAY;
    private GuiButton btnAmountAY;
    private GuiButton btnIncAY;
    private GuiButton btnDecAZ;
    private GuiButton btnAmountAZ;
    private GuiButton btnIncAZ;
    private GuiButton btnPointB;
    private GuiButton btnDecBX;
    private GuiButton btnAmountBX;
    private GuiButton btnIncBX;
    private GuiButton btnDecBY;
    private GuiButton btnAmountBY;
    private GuiButton btnIncBY;
    private GuiButton btnDecBZ;
    private GuiButton btnAmountBZ;
    private GuiButton btnIncBZ;
    private int incrementAX;
    private int incrementAY;
    private int incrementAZ;
    private int incrementBX;
    private int incrementBY;
    private int incrementBZ;
    private GuiButton btnEnable;
    private GuiButton btnSave;
    private GuiTextField tfFilename;
    private String filename;

    public GuiSchematicSave(GuiScreen guiscreen)
    {
        centerX = 0;
        centerY = 0;
        btnPointA = null;
        btnDecAX = null;
        btnAmountAX = null;
        btnIncAX = null;
        btnDecAY = null;
        btnAmountAY = null;
        btnIncAY = null;
        btnDecAZ = null;
        btnAmountAZ = null;
        btnIncAZ = null;
        btnPointB = null;
        btnDecBX = null;
        btnAmountBX = null;
        btnIncBX = null;
        btnDecBY = null;
        btnAmountBY = null;
        btnIncBY = null;
        btnDecBZ = null;
        btnAmountBZ = null;
        btnIncBZ = null;
        incrementAX = 0;
        incrementAY = 0;
        incrementAZ = 0;
        incrementBX = 0;
        incrementBY = 0;
        incrementBZ = 0;
        btnEnable = null;
        btnSave = null;
        tfFilename = null;
        filename = "";
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
        btnPointA = new GuiButton(i++, centerX - 130, centerY - 55, 100, 20, "Red point");
        controlList.add(btnPointA);
        btnDecAX = new GuiButton(i++, centerX - 130, centerY - 30, 30, 20, "-");
        controlList.add(btnDecAX);
        btnAmountAX = new GuiButton(i++, centerX - 95, centerY - 30, 30, 20, Integer.toString(settings.increments[incrementAX]));
        controlList.add(btnAmountAX);
        btnIncAX = new GuiButton(i++, centerX - 60, centerY - 30, 30, 20, "+");
        controlList.add(btnIncAX);
        btnDecAY = new GuiButton(i++, centerX - 130, centerY - 5, 30, 20, "-");
        controlList.add(btnDecAY);
        btnAmountAY = new GuiButton(i++, centerX - 95, centerY - 5, 30, 20, Integer.toString(settings.increments[incrementAY]));
        controlList.add(btnAmountAY);
        btnIncAY = new GuiButton(i++, centerX - 60, centerY - 5, 30, 20, "+");
        controlList.add(btnIncAY);
        btnDecAZ = new GuiButton(i++, centerX - 130, centerY + 20, 30, 20, "-");
        controlList.add(btnDecAZ);
        btnAmountAZ = new GuiButton(i++, centerX - 95, centerY + 20, 30, 20, Integer.toString(settings.increments[incrementAZ]));
        controlList.add(btnAmountAZ);
        btnIncAZ = new GuiButton(i++, centerX - 60, centerY + 20, 30, 20, "+");
        controlList.add(btnIncAZ);
        btnPointB = new GuiButton(i++, centerX + 30, centerY - 55, 100, 20, "Blue point");
        controlList.add(btnPointB);
        btnDecBX = new GuiButton(i++, centerX + 30, centerY - 30, 30, 20, "-");
        controlList.add(btnDecBX);
        btnAmountBX = new GuiButton(i++, centerX + 65, centerY - 30, 30, 20, Integer.toString(settings.increments[incrementBX]));
        controlList.add(btnAmountBX);
        btnIncBX = new GuiButton(i++, centerX + 100, centerY - 30, 30, 20, "+");
        controlList.add(btnIncBX);
        btnDecBY = new GuiButton(i++, centerX + 30, centerY - 5, 30, 20, "-");
        controlList.add(btnDecBY);
        btnAmountBY = new GuiButton(i++, centerX + 65, centerY - 5, 30, 20, Integer.toString(settings.increments[incrementBY]));
        controlList.add(btnAmountBY);
        btnIncBY = new GuiButton(i++, centerX + 100, centerY - 5, 30, 20, "+");
        controlList.add(btnIncBY);
        btnDecBZ = new GuiButton(i++, centerX + 30, centerY + 20, 30, 20, "-");
        controlList.add(btnDecBZ);
        btnAmountBZ = new GuiButton(i++, centerX + 65, centerY + 20, 30, 20, Integer.toString(settings.increments[incrementBZ]));
        controlList.add(btnAmountBZ);
        btnIncBZ = new GuiButton(i++, centerX + 100, centerY + 20, 30, 20, "+");
        controlList.add(btnIncBZ);
        btnEnable = new GuiButton(i++, width - 210, height - 30, 50, 20, settings.isRenderingGuide ? "Disable" : "Enable");
        controlList.add(btnEnable);
        tfFilename = new GuiTextField(fontRenderer, width - 155, height - 29, 100, 18);
        btnSave = new GuiButton(i++, width - 50, height - 30, 40, 20, "Save");
        btnSave.enabled = settings.isRenderingGuide;
        controlList.add(btnSave);
        tfFilename.setMaxStringLength(20);
        tfFilename.setText(filename);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id == btnPointA.id)
            {
                settings.moveHere(settings.pointA);
                settings.updatePoints();
            }
            else if (guibutton.id == btnDecAX.id)
            {
                settings.pointA.x -= settings.increments[incrementAX];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncAX.id)
            {
                settings.pointA.x += settings.increments[incrementAX];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountAX.id)
            {
                incrementAX = (incrementAX + 1) % settings.increments.length;
                btnAmountAX.displayString = Integer.toString(settings.increments[incrementAX]);
            }
            else if (guibutton.id == btnDecAY.id)
            {
                settings.pointA.y -= settings.increments[incrementAY];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncAY.id)
            {
                settings.pointA.y += settings.increments[incrementAY];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountAY.id)
            {
                incrementAY = (incrementAY + 1) % settings.increments.length;
                btnAmountAY.displayString = Integer.toString(settings.increments[incrementAY]);
            }
            else if (guibutton.id == btnDecAZ.id)
            {
                settings.pointA.z -= settings.increments[incrementAZ];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncAZ.id)
            {
                settings.pointA.z += settings.increments[incrementAZ];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountAZ.id)
            {
                incrementAZ = (incrementAZ + 1) % settings.increments.length;
                btnAmountAZ.displayString = Integer.toString(settings.increments[incrementAZ]);
            }
            else if (guibutton.id == btnPointB.id)
            {
                settings.moveHere(settings.pointB);
                settings.updatePoints();
            }
            else if (guibutton.id == btnDecBX.id)
            {
                settings.pointB.x -= settings.increments[incrementBX];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncBX.id)
            {
                settings.pointB.x += settings.increments[incrementBX];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountBX.id)
            {
                incrementBX = (incrementBX + 1) % settings.increments.length;
                btnAmountBX.displayString = Integer.toString(settings.increments[incrementBX]);
            }
            else if (guibutton.id == btnDecBY.id)
            {
                settings.pointB.y -= settings.increments[incrementBY];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncBY.id)
            {
                settings.pointB.y += settings.increments[incrementBY];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountBY.id)
            {
                incrementBY = (incrementBY + 1) % settings.increments.length;
                btnAmountBY.displayString = Integer.toString(settings.increments[incrementBY]);
            }
            else if (guibutton.id == btnDecBZ.id)
            {
                settings.pointB.z -= settings.increments[incrementBZ];
                settings.updatePoints();
            }
            else if (guibutton.id == btnIncBZ.id)
            {
                settings.pointB.z += settings.increments[incrementBZ];
                settings.updatePoints();
            }
            else if (guibutton.id == btnAmountBZ.id)
            {
                incrementBZ = (incrementBZ + 1) % settings.increments.length;
                btnAmountBZ.displayString = Integer.toString(settings.increments[incrementBZ]);
            }
            else if (guibutton.id == btnEnable.id)
            {
                settings.isRenderingGuide = !settings.isRenderingGuide;
                btnEnable.displayString = settings.isRenderingGuide ? "Disable" : "Enable";
                btnSave.enabled = settings.isRenderingGuide;
            }
            else if (guibutton.id == btnSave.id)
            {
                String s = (new File(Settings.schematicDirectory, (new StringBuilder()).append(tfFilename.getText()).append(".schematic").toString())).getAbsolutePath();

                if (settings.saveSchematic(s, settings.pointMin, settings.pointMax))
                {
                    filename = "";
                    tfFilename.setText(filename);
                }
            }
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int i, int j, int k)
    {
        tfFilename.mouseClicked(i, j, k);
        super.mouseClicked(i, j, k);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
        tfFilename.func_50037_a(c, i);
        filename = tfFilename.getText();
        super.keyTyped(c, i);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        tfFilename.updateCursorCounter();
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawString(fontRenderer, "Save the selection as a schematic", width - 205, height - 45, 0xffffff);
        drawString(fontRenderer, "X", centerX - 145, centerY - 24, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointA.x), centerX - 25, centerY - 24, 0xffffff);
        drawString(fontRenderer, "Y", centerX - 145, centerY + 1, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointA.y), centerX - 25, centerY + 1, 0xffffff);
        drawString(fontRenderer, "Z", centerX - 145, centerY + 26, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointA.z), centerX - 25, centerY + 26, 0xffffff);
        drawString(fontRenderer, "X", centerX + 15, centerY - 24, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointB.x), centerX + 135, centerY - 24, 0xffffff);
        drawString(fontRenderer, "Y", centerX + 15, centerY + 1, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointB.y), centerX + 135, centerY + 1, 0xffffff);
        drawString(fontRenderer, "Z", centerX + 15, centerY + 26, 0xffffff);
        drawString(fontRenderer, Integer.toString(settings.pointB.z), centerX + 135, centerY + 26, 0xffffff);
        tfFilename.drawTextBox();
        super.drawScreen(i, j, f);
    }
}
