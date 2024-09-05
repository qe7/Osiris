package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.events.impl.render.RenderThirdPersonClipEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;

public class CameraModule extends Module {

    private static final DoubleSetting cameraDistance = new DoubleSetting("Distance", 4.0, 1.0, 10.0, 0.1);

    private final BooleanSetting cameraClip = new BooleanSetting("Camera Clip", false);

    public CameraModule() {
        super("Camera", "Modifies the third person camera", ModuleCategory.RENDER);
    }

    @Subscribe
    public final Listener<RenderThirdPersonClipEvent> renderThirdPersonClipEventListener = new Listener<>(RenderThirdPersonClipEvent.class, event -> {
        if (cameraClip.getValue()) {
            event.setCancelled(true);
        }
    });

    public static float getCameraDistance() {
        return cameraDistance.getValue().floatValue();
    }
}
