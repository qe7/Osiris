package io.github.qe7.features.modules.impl.render;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderInsideBlockOverlayEvent;
import io.github.qe7.events.impl.render.RenderPortalOverlayEvent;
import io.github.qe7.events.impl.render.RenderPumpkinOverlayEvent;
import io.github.qe7.events.impl.render.RenderWaterOverlayEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;

public class NoRenderModule extends Module {

    private final BooleanSetting noPumpkinOverlay = new BooleanSetting("No Pumpkin Overlay", true);
    private final BooleanSetting noPortalOverlay = new BooleanSetting("No Portal Overlay", true);
    private final BooleanSetting noWaterOverlay = new BooleanSetting("No Water Overlay", true);
    private final BooleanSetting noInsideBlockOverlay = new BooleanSetting("No Inside Block Overlay", true);

    public NoRenderModule() {
        super("No Render", "Disabled rendering for shit", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderPumpkinOverlayEvent> renderPumpkinOverlayEventListener = event -> event.setCancelled(noPumpkinOverlay.getValue());

    @EventLink
    public final Listener<RenderPortalOverlayEvent> renderPortalOverlayEventListener = event -> event.setCancelled(noPortalOverlay.getValue());

    @EventLink
    public final Listener<RenderWaterOverlayEvent> renderWaterOverlayEventListener = event -> event.setCancelled(noWaterOverlay.getValue());

    @EventLink
    public final Listener<RenderInsideBlockOverlayEvent> renderInsideBlockOverlayEventListener = event -> event.setCancelled(noInsideBlockOverlay.getValue());
}
