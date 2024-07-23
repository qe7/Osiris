package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.events.impl.render.RenderInsideBlockOverlayEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.MovementUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet10Flying;
import org.lwjgl.input.Keyboard;

public class FreeCamModule extends Module {

    private final Minecraft mc = Minecraft.getMinecraft();

    private double x, y, z;

    private float yaw, pitch;

    public FreeCamModule() {
        super("Free Cam", "Out of body experience", ModuleCategory.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.thePlayer == null) return;

        this.x = mc.thePlayer.posX;
        this.y = mc.thePlayer.posY;
        this.z = mc.thePlayer.posZ;

        this.yaw = mc.thePlayer.rotationYaw;
        this.pitch = mc.thePlayer.rotationPitch;

        mc.thePlayer.noClip = true;

        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.thePlayer == null) return;

        mc.thePlayer.noClip = false;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.setPositionAndRotation(this.x, this.y, this.z, this.yaw, this.pitch);
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = event -> {
        if (mc.thePlayer == null) return;

        mc.thePlayer.noClip = true;
        mc.thePlayer.onGround = false;

        mc.thePlayer.motionY = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            mc.thePlayer.motionY += 0.5;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.motionY -= 0.5;
        }

        if (MovementUtility.isMoving()) {
            MovementUtility.setSpeed(0.5);
        } else {
            MovementUtility.setSpeed(0.0);
        }
    };

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (mc.thePlayer == null) return;

        if (event.getPacket() instanceof Packet10Flying) {
            event.setCancelled(true);
        }
    };

    @EventLink
    public final Listener<RenderInsideBlockOverlayEvent> renderInsideBlockOverlayEventListener = event -> {
        event.setCancelled(true);
    };
}
