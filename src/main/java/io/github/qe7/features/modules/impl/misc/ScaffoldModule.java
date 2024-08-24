package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Vec3D;

public class ScaffoldModule extends Module {

    public static BooleanSetting safeWalk = new BooleanSetting("SafeWalk", true);

    public ScaffoldModule() {
        super("Scaffold", "Automatically places blocks under the player", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<MotionEvent> motionEventListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        Vec3D vec = new Vec3D(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        Vec3D pos = new Vec3D(vec.xCoord, vec.yCoord - 2, vec.zCoord);

        if (mc.thePlayer.inventory.getCurrentItem() == null) return;
        if (mc.thePlayer.inventory.getCurrentItem().getItem() == null && !(mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBlock)) return;

        if (!isAirOrLiquid(pos)) return;

        event.setYaw(mc.thePlayer.rotationYaw + 180);
        event.setPitch(90);

        BlockPlacement placement = this.getPossiblePlacement(pos);

        if (placement == null) return;

        mc.thePlayer.swingItem();
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), placement.getX(), placement.getY(), placement.getZ(), placement.getSide());
    };

    private boolean isAirOrLiquid(Vec3D pos) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld.getBlockId((int) pos.xCoord, (int) pos.yCoord, (int) pos.zCoord) == 0) return true;

        return mc.theWorld.getBlockMaterial((int) pos.xCoord, (int) pos.yCoord, (int) pos.zCoord).isLiquid();
    }

    private BlockPlacement getPossiblePlacement(Vec3D pos) {
        Vec3D posCopy = new Vec3D(pos.xCoord, pos.yCoord, pos.zCoord);
        Minecraft mc = Minecraft.getMinecraft();

        int x = (int) posCopy.xCoord;
        int y = (int) posCopy.yCoord;
        int z = (int) posCopy.zCoord;

        // yes this method is shit code, fuck you and fuck Java. - shae
        // check sides

        if (!this.isAirOrLiquid(new Vec3D(x, y - 1, z))) return new BlockPlacement(posCopy.offset(0, -1, 0), 1);

        if (!this.isAirOrLiquid(new Vec3D(x + 1, y, z))) return new BlockPlacement(posCopy.offset(1, 0, 0), 4);

        if (!this.isAirOrLiquid(new Vec3D(x - 1, y, z))) return new BlockPlacement(posCopy.offset(-1, 0, 0), 5);

        if (!this.isAirOrLiquid(new Vec3D(x, y, z + 1))) return new BlockPlacement(posCopy.offset(0, 0, 1), 2);

        if (!this.isAirOrLiquid(new Vec3D(x, y, z - 1))) return new BlockPlacement(posCopy.offset(0, 0, -1), 3);

        if (!this.isAirOrLiquid(new Vec3D(x, y + 1, z))) return new BlockPlacement(posCopy.offset(0, 1, 0), 0);

        // check diagonals

        if (!this.isAirOrLiquid(new Vec3D(x + 1, y, z + 1))) return new BlockPlacement(posCopy.offset(1, 0, 1), 2);

        if (!this.isAirOrLiquid(new Vec3D(x - 1, y, z - 1))) return new BlockPlacement(posCopy.offset(-1, 0, -1), 3);

        if (!this.isAirOrLiquid(new Vec3D(x - 1, y, z - 1))) return new BlockPlacement(posCopy.offset(1, 0, -1), 3);

        if (!this.isAirOrLiquid(new Vec3D(x - 1, y, z - 1))) return new BlockPlacement(posCopy.offset(-1, 0, 1), 2);

        // back and down

        if (!this.isAirOrLiquid(new Vec3D(x, y - 1, z + 1))) return new BlockPlacement(posCopy.offset(0, -1, 1), 2);

        if (!this.isAirOrLiquid(new Vec3D(x, y - 1, z - 1))) return new BlockPlacement(posCopy.offset(0, -1, -1), 3);

        if (!this.isAirOrLiquid(new Vec3D(x + 1, y - 1, z))) return new BlockPlacement(posCopy.offset(1, -1, 0), 4);

        if (!this.isAirOrLiquid(new Vec3D(x - 1, y - 1, z))) return new BlockPlacement(posCopy.offset(-1, -1, 0), 5);

        return null;
    }

    private static class BlockPlacement {
        private final Vec3D pos;
        private final int side;

        public BlockPlacement(Vec3D pos, int side) {
            this.pos = pos;
            this.side = side;
        }

        public int getX() {
            return (int) pos.xCoord;
        }

        public int getY() {
            return (int) pos.yCoord;
        }

        public int getZ() {
            return (int) pos.zCoord;
        }

        public int getSide() {
            return side;
        }
    }
}
