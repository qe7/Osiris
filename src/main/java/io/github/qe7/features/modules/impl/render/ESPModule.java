package io.github.qe7.features.modules.impl.render;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderEntityEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.utils.render.OpenGLRenderUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ESPModule extends Module {

    private final BooleanSetting entities = new BooleanSetting("Entities", true);
    private final BooleanSetting players = new BooleanSetting("Players", true).supplyIf(entities::getValue);
    private final BooleanSetting animals = new BooleanSetting("Animals", true).supplyIf(entities::getValue);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", true).supplyIf(entities::getValue);
    private final BooleanSetting items = new BooleanSetting("Items", true).supplyIf(entities::getValue);

    private final BooleanSetting blocks = new BooleanSetting("Blocks", true);
    private final BooleanSetting chests = new BooleanSetting("Chests", true).supplyIf(blocks::getValue);
    private final BooleanSetting spawners = new BooleanSetting("Spawners", true).supplyIf(blocks::getValue);
    private final BooleanSetting endPortals = new BooleanSetting("End Portals", true).supplyIf(blocks::getValue);
    private final BooleanSetting enchantmentTables = new BooleanSetting("Enchantment Tables", true).supplyIf(blocks::getValue);

    public ESPModule() {
        super("ESP", "Displays shit.. idfk.", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderEntityEvent> renderEntityListener = event -> {
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        if (entities.getValue()) {
            final List<Entity> entities = new ArrayList<>(Minecraft.getMinecraft().theWorld.loadedEntityList);

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

                final AxisAlignedBB boundingBox = entity.boundingBox;

                final double width = boundingBox.maxX - boundingBox.minX;
                final double height = boundingBox.maxY - boundingBox.minY;

                OpenGLRenderUtility.drawEntityESP(x, y, z, width, height + 0.2f, this.getEntityColor(entity), 1.0f);
            }
        }

        if (blocks.getValue()) {
            final List<TileEntity> tileEntities = new ArrayList<>(Minecraft.getMinecraft().theWorld.loadedTileEntityList);

            tileEntities.removeIf(tileEntity -> {
                if (tileEntity instanceof TileEntityChest) {
                    return !chests.getValue();
                }
                if (tileEntity instanceof TileEntityMobSpawner) {
                    return !spawners.getValue();
                }
                if (tileEntity instanceof TileEntityEndPortal) {
                    return !endPortals.getValue();
                }
                if (tileEntity instanceof TileEntityEnchantmentTable) {
                    return !enchantmentTables.getValue();
                }
                return true;
            });

            for (TileEntity tileEntity : tileEntities) {
                final double x = tileEntity.xCoord - RenderManager.renderPosX;
                final double y = tileEntity.yCoord - RenderManager.renderPosY;
                final double z = tileEntity.zCoord - RenderManager.renderPosZ;

                OpenGLRenderUtility.drawBlockESP(x, y, z, this.getTileEntityColor(tileEntity), 1.0f);
            }
        }
    };

    private Color getEntityColor(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (Osiris.getInstance().getFriendManager().isFriend(((EntityPlayer) entity).username)) {
                return new Color(63, 124, 182);
            } else {
                return new Color(182, 63, 63);
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

    private Color getTileEntityColor(TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest) {
            return new Color(199, 141, 75);
        }
        if (tileEntity instanceof TileEntityMobSpawner) {
            return new Color(75, 30, 77);
        }
        if (tileEntity instanceof TileEntityEndPortal) {
            return new Color(27, 94, 32);
        }
        if (tileEntity instanceof TileEntityEnchantmentTable) {
            return new Color(74, 20, 140);
        }
        return new Color(150, 150, 150);
    }
}
