package lunatrius.schematica;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.game.KeyInputEvent;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.KeyBinding;

public class SchematicaLoader {
	private static final Settings settings = Settings.instance();
	
	@Subscribe
    public static final Listener<KeyInputEvent> inputHandler = new Listener<>(KeyInputEvent.class, event -> {
    	SchematicaLoader.settings.keyboardEvent_kc(event.getKeyCode());
    });
	
    public static void preInit()
    {
    	//XXX some not existing here forge stuff
    	//Configuration configuration = new Configuration(new File(Minecraft.getMinecraftDir(), "/config/Schematica.cfg"));
        //configuration.load();
        //settings.enableAlpha = Config.getBoolean(configuration, "alphaEnabled", "general", settings.enableAlpha, "Enable transparent textures.");
        //settings.alpha = (float)Config.getInt(configuration, "alpha", "general", (int)(settings.alpha * 255F), 0, 255, "Alpha value used when rendering the schematic.") / 255F;
        //settings.highlight = Config.getBoolean(configuration, "highlight", "general", settings.highlight, "Highlight invalid placed blocks and to be placed blocks.");
        //settings.renderRange.x = Config.getInt(configuration, "renderRangeX", "general", settings.renderRange.x, 5, 50, "Render range along the X axis.");
        //settings.renderRange.y = Config.getInt(configuration, "renderRangeY", "general", settings.renderRange.y, 5, 50, "Render range along the Y axis.");
        //settings.renderRange.z = Config.getInt(configuration, "renderRangeZ", "general", settings.renderRange.z, 5, 50, "Render range along the Z axis.");
        //settings.blockDelta = Config.getFloat(configuration, "blockDelta", "general", settings.blockDelta, 0.0F, 0.5F, "Delta value used for highlighting (if you're having issue with overlapping textures try setting this value higher).");
        //configuration.save();
    	
        Settings.schematicDirectory.mkdirs();
        Settings.textureDirectory.mkdirs();
    }

    public static void init()
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	ArrayList<KeyBinding> keys = new ArrayList<>(Arrays.asList(mc.gameSettings.keyBindings));
    	for (int i = 0; i < settings.keyBindings.length; i++)
        {
    		keys.add(settings.keyBindings[i]);
        }
    	mc.gameSettings.keyBindings = new KeyBinding[keys.size()];
    	for(int i = 0; i < keys.size(); ++i) mc.gameSettings.keyBindings[i] = keys.get(i);
    	Osiris.getInstance().getEventBus().subscribe(inputHandler);
    }

    public static void load()
    {
        preInit();
        init();
    }
}
