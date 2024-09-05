package net.minecraft.src;

import io.github.qe7.Osiris;
import io.github.qe7.features.impl.modules.impl.misc.AutoReconnectModule;

public class GuiDisconnected extends GuiScreen
{
    /** The error message. */
    private String errorMessage;

    /** The details about the error. */
    private String errorDetail;

    public GuiDisconnected(String par1Str, String par2Str, Object par3ArrayOfObj[])
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        errorMessage = stringtranslate.translateKey(par1Str);

        if (par3ArrayOfObj != null)
        {
            errorDetail = stringtranslate.translateKeyFormat(par2Str, par3ArrayOfObj);
        }
        else
        {
            errorDetail = stringtranslate.translateKey(par2Str);
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.toMenu")));
        if(Osiris.getInstance().getModuleManager().getMap().get(AutoReconnectModule.class).isEnabled()) {
        	controlList.add(new GuiSmallButton(1, width / 2 - 75, height / 4 + 80 + 12, "Stop auto reconnect"));
        } else
        	controlList.add(new GuiSmallButton(1, width / 2 - 75, height / 4 + 80 + 12, "Start auto reconnect"));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(new GuiMainMenu());
        }
        if (par1GuiButton.id == 1)
        {
        	if(Osiris.getInstance().getModuleManager().getMap().get(AutoReconnectModule.class).isEnabled()) {
        		Osiris.getInstance().getModuleManager().getMap().get(AutoReconnectModule.class).setEnabled(false);
        		this.mc.needToReconnect = false;
        		((GuiSmallButton)this.controlList.get(1)).displayString = "Start auto reconnect";
        	} else {
        		Osiris.getInstance().getModuleManager().getMap().get(AutoReconnectModule.class).setEnabled(true);
        		this.mc.needToReconnect = true;
        		((GuiSmallButton)this.controlList.get(1)).displayString = "Stop auto reconnect";
        	}
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, errorMessage, width / 2, height / 2 - 50, 0xffffff);
        drawCenteredString(fontRenderer, errorDetail, width / 2, height / 2 - 10, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
