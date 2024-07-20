package io.github.qe7.utils.local;

import io.github.qe7.utils.UtilityBase;
import net.minecraft.client.Minecraft;

/**
 * Movement Utility class for movement-related methods
 */
public final class MovementUtility extends UtilityBase {

    /**
     * Checks if the thePlayer is moving
     *
     * @return whether the thePlayer is moving
     */
    public static boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer.movementInput.moveForward != 0.0F || Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe != 0.0F;
    }

    /**
     * Sets the thePlayer's speed
     *
     * @param moveSpeed the speed to set
     */
    public static void setSpeed(double moveSpeed) {
        setSpeed(moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe, Minecraft.getMinecraft().thePlayer.movementInput.moveForward);
    }

    /**
     * Sets the thePlayer's speed
     *
     * @param moveSpeed the speed to set
     * @param yaw       the yaw to set
     * @param strafe    the strafe to set
     * @param forward   the forward to set
     */
    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double motionX = Math.cos(Math.toRadians((yaw + 90.0F)));
        double motionZ = Math.sin(Math.toRadians((yaw + 90.0F)));
        Minecraft.getMinecraft().thePlayer.motionX = forward * moveSpeed * motionX + strafe * moveSpeed * motionZ;
        Minecraft.getMinecraft().thePlayer.motionZ = forward * moveSpeed * motionZ - strafe * moveSpeed * motionX;
    }
}
