package io.github.qe7.features.modules.impl.render;

import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;

public class XRayModule extends Module {

    private static final int[] blockIDs = { 8, 9, 10, 11, 14, 15, 16, 21, 22, 41, 42, 52, 54, 56, 57, 73, 74 };

    public XRayModule() {
        super("XRay", "Only renders desired blocks", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }

    public static boolean isBlockID(int id) {
        for (int blockID : blockIDs) {
            if (blockID == id) {
                return true;
            }
        }
        return false;
    }
}
