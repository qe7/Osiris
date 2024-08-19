package io.github.qe7.features.modules.impl.render;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.*;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
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

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenEventListener = event -> {
        if (!noWeather.getValue()) return;
        if (Minecraft.getMinecraft().theWorld == null) return;

        Minecraft.getMinecraft().theWorld.rainingStrength = 0;
        Minecraft.getMinecraft().theWorld.prevRainingStrength = 0;
        Minecraft.getMinecraft().theWorld.thunderingStrength = 0;
        Minecraft.getMinecraft().theWorld.prevThunderingStrength = 0;
    };

    @EventLink
    public final Listener<RenderPumpkinOverlayEvent> renderPumpkinOverlayEventListener = event -> event.setCancelled(noPumpkinOverlay.getValue());

    @EventLink
    public final Listener<RenderPortalOverlayEvent> renderPortalOverlayEventListener = event -> event.setCancelled(noPortalOverlay.getValue());

    @EventLink
    public final Listener<RenderWaterOverlayEvent> renderWaterOverlayEventListener = event -> event.setCancelled(noWaterOverlay.getValue());

    @EventLink
    public final Listener<RenderInsideBlockOverlayEvent> renderInsideBlockOverlayEventListener = event -> event.setCancelled(noInsideBlockOverlay.getValue());

    @EventLink
    public final Listener<RenderFireFirstPersonEvent> renderFireFirstPersonEventListener = event -> event.setCancelled(noFireOverlay.getValue());

    @EventLink
    public final Listener<RenderGuiAchievementEvent> renderGuiAchievementEventListener = event -> event.setCancelled(achievementNotice.getValue());

    @EventLink
    public final Listener<RenderHurtCamEvent> renderHurtCamEventListener = event -> event.setCancelled(noHurtCameraEffect.getValue());

    @EventLink
    public final Listener<RenderBlockBreakingParticlesEvent> renderBlockBreakingParticlesEventListener = event -> event.setCancelled(noBlockBreakingParticles.getValue());
}
