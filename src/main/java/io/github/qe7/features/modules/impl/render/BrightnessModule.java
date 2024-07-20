package io.github.qe7.features.modules.impl.render;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
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

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenListener = event -> Minecraft.getMinecraft().gameSettings.gammaSetting = 1000;
}
