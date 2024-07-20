package io.github.qe7.features.modules.impl.movement;

import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiInventory;
import org.lwjgl.input.Keyboard;

public class InventoryMoveModule extends Module {

    public InventoryMoveModule() {
        super("Inventory Move", "Allows you to move in guis", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        updateKeyBinds();
    }

    @EventLink
    public final Listener<LivingUpdateEvent> livingUpdateListener = event -> {
        if (Minecraft.getMinecraft().currentScreen != null && (Minecraft.getMinecraft().currentScreen instanceof GuiInventory || Minecraft.getMinecraft().currentScreen instanceof GuiContainer)) {
            updateKeyBinds();
        }
    };

    public void updateKeyBinds() {
        Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward.keyCode);
        Minecraft.getMinecraft().gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindBack.keyCode);
        Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindLeft.keyCode);
        Minecraft.getMinecraft().gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindRight.keyCode);
        Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.keyCode);
    }
}
