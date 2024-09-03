package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;

public class BrightnessModule extends Module {

    private float oldGamma;

    public BrightnessModule() {
        super("Brightness", "Brightens up your life", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Minecraft.getMinecraft().gameSettings.gammaSetting = oldGamma;
    }

    @Subscribe
    public final Listener<RenderScreenEvent> renderScreenListener = new Listener<>(event -> {
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1000.0f;
    });
}
