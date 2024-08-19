package io.github.qe7.features.modules.impl.movement;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.EnumSetting;
import io.github.qe7.features.modules.api.settings.impl.interfaces.IEnumSetting;
import io.github.qe7.utils.local.MovementUtil;
import net.minecraft.client.Minecraft;

public class SpeedModule extends Module {

    private final EnumSetting<Mode> mode = new EnumSetting<>("Mode", Mode.STRAFE);

    private final BooleanSetting timer = new BooleanSetting("Timer", false);

    private final BooleanSetting waterCheck = new BooleanSetting("Water Check", true);

    public SpeedModule() {
        super("Speed", "Speeds up your life", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (Minecraft.timer.timerSpeed != 1.0F) {
            Minecraft.timer.timerSpeed = 1.0F;
        }
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;
        if (!MovementUtil.isMoving()) return;
        if (mc.thePlayer.isInWater() && this.waterCheck.getValue()) return;

        this.setSuffix(mode.getValue().getName());
        mc.thePlayer.setSprinting(true);

        switch (mode.getValue()) {
            case STRAFE: {
                MovementUtil.setSpeed(0.28);
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }
                break;
            }
            case LOW_HOP: {
                MovementUtil.setSpeed(0.29 + (mc.thePlayer.motionY < 0 ? 0.028 : 0.0));
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = mc.thePlayer.isCollidedHorizontally ? 0.42 : 0.2;
                }
                break;
            }
        }

        if (timer.getValue()) {
            if (Minecraft.getMinecraft().thePlayer.motionY < 0.0D) {
                Minecraft.timer.timerSpeed = 1.2F;
            }
        }
    };

    private enum Mode implements IEnumSetting {
        STRAFE("Strafe"), LOW_HOP("LowHop");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return Mode.valueOf(name);
        }
    }
}
