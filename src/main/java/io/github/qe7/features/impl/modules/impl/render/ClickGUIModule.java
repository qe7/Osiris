package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.uis.clickGui.impl.GuiClick;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

    private GuiClick guiClick;

    public ClickGUIModule() {
        super("Click GUI", "Displays a clickable GUI to manage modules", ModuleCategory.RENDER);

        this.setKeyCode(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (this.guiClick == null) {
            this.guiClick = new GuiClick();
        }

        if (Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().displayGuiScreen(this.guiClick);
        }

        this.setEnabled(false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
