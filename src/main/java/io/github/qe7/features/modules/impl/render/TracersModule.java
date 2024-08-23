package io.github.qe7.features.modules.impl.render;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderEntityEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.utils.render.OpenGLRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TracersModule extends Module {

    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true);
    private final BooleanSetting items = new BooleanSetting("Items", true);

    final List<Entity> entities = new ArrayList<>(Minecraft.getMinecraft().theWorld.loadedEntityList);

    public TracersModule() {
        super("Tracers", "Draws a line to targeted entities", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderEntityEvent> renderEntityListener = event -> {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        entities.removeIf(entity -> {
            if (entity instanceof EntityPlayer && !entity.equals(Minecraft.getMinecraft().thePlayer)) {
                return !players.getValue();
            }
            if (entity instanceof EntityAnimal) {
                return !animals.getValue();
            }
            if (entity instanceof EntityMob) {
                return !mobs.getValue();
            }
            if (entity instanceof EntityItem) {
                return !items.getValue();
            }
            return true;
        });

        for (Entity entity : entities) {
            final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosX;
            final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosY;
            final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.timer.renderPartialTicks - RenderManager.renderPosZ;

            OpenGLRenderUtil.drawTracerLine(x, y, z, this.getEntityColor(entity), 1.0f);
        }
    };

    private Color getEntityColor(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (Osiris.getInstance().getRelationManager().isFriend(((EntityPlayer) entity).username)) {
                return new Color(63, 124, 182);
            } else if (Osiris.getInstance().getRelationManager().isEnemy(((EntityPlayer) entity).username)) {
                return new Color(182, 63, 63);
            } else {
                return new Color(182, 182, 63);
            }
        }
        if (entity instanceof EntityAnimal) {
            return new Color(164, 95, 56);
        }
        if (entity instanceof EntityMob) {
            return new Color(107, 44, 126);
        }
        return new Color(150, 150, 150);
    }
}
