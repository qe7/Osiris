package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.events.impl.render.RenderInsideBlockOverlayEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.MovementUtil;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
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

    @Subscribe
    public final Listener<LivingUpdateEvent> livingUpdateEventListener = new Listener<>(LivingUpdateEvent.class, event -> {
        if (mc.thePlayer == null) return;

        mc.thePlayer.noClip = true;
        mc.thePlayer.onGround = false;

        mc.thePlayer.motionY = 0;

        if (mc.currentScreen != null) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            mc.thePlayer.motionY += 0.5;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            mc.thePlayer.motionY -= 0.5;
        }

        if (MovementUtil.isMoving()) {
            MovementUtil.setSpeed(0.5);
        } else {
            MovementUtil.setSpeed(0.0);
        }
    });

    @Subscribe
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = new Listener<>(OutgoingPacketEvent.class, event -> {
        if (mc.thePlayer == null) return;

        if (event.getPacket() instanceof Packet10Flying) {
            event.setCancelled(true);
        }
    });

    @Subscribe
    public final Listener<RenderInsideBlockOverlayEvent> renderInsideBlockOverlayEventListener = new Listener<>(RenderInsideBlockOverlayEvent.class, event -> {
        event.setCancelled(true);
    });
}
