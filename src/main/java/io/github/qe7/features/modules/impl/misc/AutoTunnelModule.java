package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.events.impl.render.RenderEntityEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import io.github.qe7.utils.local.MovementUtil;
import io.github.qe7.utils.render.OpenGLRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;

import java.awt.*;
import java.util.List;
import java.util.Vector;

public class AutoTunnelModule extends Module {

    private final IntSetting width = new IntSetting("Width", 1, 1, 5, 1);

    private final BooleanSetting stopWhileMining = new BooleanSetting("Stop While Mining", true);

    private final List<Vec3D> blocks = new Vector<>();

    private Vec3D targetBlock;

    public AutoTunnelModule() {
        super("Auto Tunnel", "Automatically breaks a tunnel", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        blocks.clear();
        targetBlock = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventLink
    public final Listener<RenderEntityEvent> renderListener = event -> {
        // loop through all blocks and render outline :D

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return;
        }

        if (blocks.isEmpty()) {
            return;
        }

        for (Vec3D block : blocks) {
            if (block == targetBlock) {
                OpenGLRenderUtil.drawBlockESP(block.xCoord, block.yCoord, block.zCoord, new Color(0, 255, 0), 1.0f);
                continue;
            }
            OpenGLRenderUtil.drawBlockESP(block.xCoord, block.yCoord, block.zCoord, new Color(255, 0, 0), 1.0f);
        }
    };

    @EventLink
    public final Listener<MotionEvent> motionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) {
            return;
        }

        Vec3D playerPos = new Vec3D(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        if (blocks.isEmpty()) {
            populateBlocks(playerPos);
        }

        if (blocks.isEmpty()) {
            return;
        }

        if (stopWhileMining.getValue()) {
            MovementUtil.setSpeed(0);
        }

        targetBlock = blocks.get(0);

        breakBlock(targetBlock);
    };

    private void populateBlocks(Vec3D origin) {
        blocks.clear();

        Vec3D originCopy = new Vec3D(origin.xCoord, origin.yCoord, origin.zCoord);

        // get the direction the player is facing and normalize it (-180 to 180)
        float yaw = this.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationYaw);

        // round to closest 90 degrees
        yaw = Math.round(yaw / 90) * 90;

        this.setSuffix("Yaw: " + yaw);

        // based off the yaw, we can determine the direction the player is facing
        switch ((int) yaw) {
            case 0:

                break;
            case 90:

                break;
            case 180:

                break;
            case -90:

                break;
        }
    }

    private float wrapAngleTo180_float(float rotationYaw) {
        rotationYaw %= 360.0F;

        if (rotationYaw >= 180.0F) {
            rotationYaw -= 360.0F;
        }

        if (rotationYaw < -180.0F) {
            rotationYaw += 360.0F;
        }

        return rotationYaw;
    }

    private void breakBlock(Vec3D targetBlock) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld.getBlockId((int) targetBlock.xCoord, (int) targetBlock.yCoord, (int) targetBlock.zCoord) == 0) {
            blocks.remove(targetBlock);
            return;
        }

        mc.playerController.clickBlock((int) targetBlock.xCoord, (int) targetBlock.yCoord, (int) targetBlock.zCoord, 0);

        mc.thePlayer.swingItem();
    }
}
