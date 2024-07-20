package net.minecraft.src;

public enum EnumOptions
{
    MUSIC("options.music", true, false),
    SOUND("options.sound", true, false),
    INVERT_MOUSE("options.invertMouse", false, true),
    SENSITIVITY("options.sensitivity", true, false),
    FOV("options.fov", true, false),
    GAMMA("options.gamma", true, false),
    RENDER_DISTANCE("options.renderDistance", false, false),
    VIEW_BOBBING("options.viewBobbing", false, true),
    ANAGLYPH("options.anaglyph", false, true),
    ADVANCED_OPENGL("options.advancedOpengl", false, true),
    FRAMERATE_LIMIT("options.framerateLimit", false, false),
    DIFFICULTY("options.difficulty", false, false),
    GRAPHICS("options.graphics", false, false),
    AMBIENT_OCCLUSION("options.ao", false, true),
    GUI_SCALE("options.guiScale", false, false),
    RENDER_CLOUDS("options.renderClouds", false, true),
    PARTICLES("options.particles", false, false);

    private final boolean enumFloat;
    private final boolean enumBoolean;
    private final String enumString;

    public static EnumOptions getEnumOptions(int par0)
    {
        EnumOptions aenumoptions[] = values();
        int i = aenumoptions.length;

        for (int j = 0; j < i; j++)
        {
            EnumOptions enumoptions = aenumoptions[j];

            if (enumoptions.returnEnumOrdinal() == par0)
            {
                return enumoptions;
            }
        }

        return null;
    }

    private EnumOptions(String par3Str, boolean par4, boolean par5)
    {
        enumString = par3Str;
        enumFloat = par4;
        enumBoolean = par5;
    }

    public boolean getEnumFloat()
    {
        return enumFloat;
    }

    public boolean getEnumBoolean()
    {
        return enumBoolean;
    }

    public int returnEnumOrdinal()
    {
        return ordinal();
    }

    public String getEnumString()
    {
        return enumString;
    }
}
