package io.github.qe7.features.modules.impl.render;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderLivingLabelEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.utils.render.OpenGLRenderUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;

public class NameTagsModule extends Module {

    private final DoubleSetting scale = new DoubleSetting("Scale", 2.0, 0.5, 5.0, 0.1);

    public NameTagsModule() {
        super("Name Tags", "Displays a name plate", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderLivingLabelEvent> renderLivingLabelEventListener = event -> {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        if (!(event.getEntity() instanceof EntityPlayer)) return;

        final EntityPlayer player = (EntityPlayer) event.getEntity();

        OpenGLRenderUtility.drawName(player, event.getRenderManager(), event.getX(), event.getY(), event.getZ(), scale.getValue().floatValue());
        event.setCancelled(true);
    };
}
