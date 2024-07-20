package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.packet.OutgoingPacketEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet14BlockDig;

public class AutoToolModule extends Module {

    public AutoToolModule() {
        super("Auto Tool", "Automatically switches to the best tool", ModuleCategory.MISC);
    }

    @EventLink
    public final Listener<OutgoingPacketEvent> outgoingPacketEventListener = event -> {
        if (event.getPacket() instanceof Packet14BlockDig) {

            final Packet14BlockDig blockDig = (Packet14BlockDig) event.getPacket();

            final int blockID = Minecraft.getMinecraft().theWorld.getBlockId(blockDig.xPosition, blockDig.yPosition, blockDig.zPosition);

            if (blockID != 0) {
                float maxStrength = 0.1f;
                int bestToolSlot = 0;

                for (int i = 0; i < 9; i++) {
                    ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
                    if (itemStack != null) {
                        float strength = itemStack.getStrVsBlock(Block.blocksList[blockID]);
                        if (strength > maxStrength) {
                            maxStrength = strength;
                            bestToolSlot = i;
                        }
                    }
                }

                Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestToolSlot;
            }
        }
    };
}
