package lunatrius.schematica;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.Sys;

public class GuiSchematicLoad extends GuiScreen
{
    private final Settings settings = Settings.instance();
    protected GuiScreen prevGuiScreen;
    private GuiSchematicLoadSlot schematicGuiChooserSlot;
    private GuiSmallButton btnOpenDir;
    private GuiSmallButton btnDone;

    public GuiSchematicLoad(GuiScreen guiscreen)
    {
        btnOpenDir = null;
        btnDone = null;
        prevGuiScreen = guiscreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        int i = 0;
        btnOpenDir = new GuiSmallButton(i++, width / 2 - 154, height - 48, "Open schematic folder");
        controlList.add(btnOpenDir);
        btnDone = new GuiSmallButton(i++, width / 2 + 4, height - 48, "Done");
        controlList.add(btnDone);
        schematicGuiChooserSlot = new GuiSchematicLoadSlot(this);
        schematicGuiChooserSlot.registerScrollButtons(controlList, 7, 8);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.enabled)
        {
            if (guibutton.id == btnOpenDir.id)
            {
                boolean flag = false;

                try
                {
                    Class class1 = Class.forName("java.awt.Desktop");
                    Object obj = class1.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                    class1.getMethod("browse", new Class[]
                            {
                                java.net.URI.class
                            }).invoke(obj, new Object[]
                                    {
                                        Settings.schematicDirectory.toURI()
                                    });
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                    flag = true;
                }

                if (flag)
                {
                    System.out.println("Opening via Sys class!");
                    Sys.openURL((new StringBuilder()).append("file://").append(Settings.schematicDirectory.getAbsolutePath()).toString());
                }
            }
            else if (guibutton.id == btnDone.id)
            {
                loadSchematic();
                mc.displayGuiScreen(prevGuiScreen);
            }
            else
            {
                schematicGuiChooserSlot.actionPerformed(guibutton);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int i, int j, float f)
    {
        schematicGuiChooserSlot.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, "Select schematic file", width / 2, 16, 0xffffff);
        drawCenteredString(fontRenderer, "(Place schematic files here)", width / 2 - 77, height - 26, 0x808080);
        super.drawScreen(i, j, f);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    }

    private void loadSchematic()
    {
        List list = settings.getSchematicFiles();

        try
        {
            if (settings.selectedSchematic <= 0 || settings.selectedSchematic >= list.size() || !settings.loadSchematic((new File(Settings.schematicDirectory, (String)list.get(settings.selectedSchematic))).getPath()))
            {
                settings.selectedSchematic = 0;
            }
        }
        catch (Exception exception)
        {
            settings.selectedSchematic = 0;
        }

        settings.moveHere();
    }
}
