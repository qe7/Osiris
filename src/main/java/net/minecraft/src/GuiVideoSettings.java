package net.minecraft.src;

public class GuiVideoSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle;

    /** GUI game settings */
    private GameSettings guiGameSettings;

    /**
     * True if the system is 64-bit (using a simple indexOf test on a system property)
     */
    private boolean is64bit;
    private static EnumOptions videoOptions[];

    public GuiVideoSettings(GuiScreen par1GuiScreen, GameSettings par2GameSettings)
    {
        screenTitle = "Video Settings";
        is64bit = false;
        parentGuiScreen = par1GuiScreen;
        guiGameSettings = par2GameSettings;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        screenTitle = stringtranslate.translateKey("options.videoTitle");
        int i = 0;
        Object aobj[] = videoOptions;
        int j = aobj.length;

        for (int k = 0; k < j; k++)
        {
            EnumOptions enumoptions = (EnumOptions) aobj[k];

            if (!enumoptions.getEnumFloat())
            {
                controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions)));
            }
            else
            {
                controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions), guiGameSettings.getOptionFloatValue(enumoptions)));
            }

            i++;
        }

        controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
        is64bit = false;
        aobj = (new String[]
                {
                    "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"
                });
        String as[] = ((String [])(aobj));
        int l = as.length;
        int i1 = 0;

        do
        {
            if (i1 >= l)
            {
                break;
            }

            String s = as[i1];
            String s1 = System.getProperty(s);

            if (s1 != null && s1.indexOf("64") >= 0)
            {
                is64bit = true;
                break;
            }

            i1++;
        }
        while (true);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        int i = guiGameSettings.guiScale;

        if (par1GuiButton.id < 100 && (par1GuiButton instanceof GuiSmallButton))
        {
            guiGameSettings.setOptionValue(((GuiSmallButton)par1GuiButton).returnEnumOptions(), 1);
            par1GuiButton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
        }

        if (par1GuiButton.id == 200)
        {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(parentGuiScreen);
        }

        if (guiGameSettings.guiScale != i)
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            int j = scaledresolution.getScaledWidth();
            int k = scaledresolution.getScaledHeight();
            setWorldAndResolution(mc, j, k);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);

        if (!is64bit && guiGameSettings.renderDistance == 0)
        {
            drawCenteredString(fontRenderer, StatCollector.translateToLocal("options.farWarning1"), width / 2, height / 6 + 144, 0xaf0000);
            drawCenteredString(fontRenderer, StatCollector.translateToLocal("options.farWarning2"), width / 2, height / 6 + 144 + 12, 0xaf0000);
        }

        super.drawScreen(par1, par2, par3);
    }

    static
    {
        videoOptions = (new EnumOptions[]
                {
                    EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.GAMMA, EnumOptions.RENDER_CLOUDS,
                    EnumOptions.PARTICLES
                });
    }
}
