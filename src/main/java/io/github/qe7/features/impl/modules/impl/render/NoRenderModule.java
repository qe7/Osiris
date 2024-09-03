package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.events.impl.render.*;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;

public class NoRenderModule extends Module {

    private final BooleanSetting noPumpkinOverlay = new BooleanSetting("Pumpkin Overlay", true);
    private final BooleanSetting noPortalOverlay = new BooleanSetting("Portal Overlay", true);
    private final BooleanSetting noWaterOverlay = new BooleanSetting("Water Overlay", true);
    private final BooleanSetting noInsideBlockOverlay = new BooleanSetting("Inside Block", true);
    private final BooleanSetting noFireOverlay = new BooleanSetting("Fire Overlay", true);
    private final BooleanSetting noHurtCameraEffect = new BooleanSetting("Hurt Camera Effect", true);
    private final BooleanSetting achievementNotice = new BooleanSetting("Achievement Notice", true);
    private final BooleanSetting noBlockBreakingParticles = new BooleanSetting("Block Breaking Particles", true);
    private final BooleanSetting noWeather = new BooleanSetting("Weather", true);

    public NoRenderModule() {
        super("No Render", "Disabled rendering for shit", ModuleCategory.RENDER);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (Minecraft.getMinecraft().theWorld == null) return;
        Minecraft.getMinecraft().theWorld.noRenderWeather = false;
    }

    @Subscribe
    public final Listener<RenderScreenEvent> renderScreenEventListener = new Listener<>(event -> {
        if (Minecraft.getMinecraft().theWorld == null) return;
        Minecraft.getMinecraft().theWorld.noRenderWeather = noWeather.getValue();
    });

    @Subscribe
    public final Listener<RenderPumpkinOverlayEvent> renderPumpkinOverlayEventListener = new Listener<>(event -> event.setCancelled(noPumpkinOverlay.getValue()));

    @Subscribe
    public final Listener<RenderPortalOverlayEvent> renderPortalOverlayEventListener = new Listener<>(event -> event.setCancelled(noPortalOverlay.getValue()));

    @Subscribe
    public final Listener<RenderWaterOverlayEvent> renderWaterOverlayEventListener = new Listener<>(event -> event.setCancelled(noWaterOverlay.getValue()));

    @Subscribe
    public final Listener<RenderInsideBlockOverlayEvent> renderInsideBlockOverlayEventListener = new Listener<>(event -> event.setCancelled(noInsideBlockOverlay.getValue()));

    @Subscribe
    public final Listener<RenderFireFirstPersonEvent> renderFireFirstPersonEventListener = new Listener<>(event -> event.setCancelled(noFireOverlay.getValue()));

    @Subscribe
    public final Listener<RenderGuiAchievementEvent> renderGuiAchievementEventListener = new Listener<>(event -> event.setCancelled(achievementNotice.getValue()));

    @Subscribe
    public final Listener<RenderHurtCamEvent> renderHurtCamEventListener = new Listener<>(event -> event.setCancelled(noHurtCameraEffect.getValue()));

    @Subscribe
    public final Listener<RenderBlockBreakingParticlesEvent> renderBlockBreakingParticlesEventListener = new Listener<>(event -> event.setCancelled(noBlockBreakingParticles.getValue()));
}
