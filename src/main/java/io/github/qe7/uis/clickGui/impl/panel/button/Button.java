package io.github.qe7.uis.clickGui.impl.panel.button;

import io.github.qe7.Osiris;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.settings.api.Setting;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.features.modules.api.settings.impl.EnumSetting;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import io.github.qe7.features.modules.impl.render.HUDModule;
import io.github.qe7.uis.clickGui.api.types.Component;
import io.github.qe7.uis.clickGui.impl.panel.button.component.BooleanComponent;
import io.github.qe7.uis.clickGui.impl.panel.button.component.DoubleComponent;
import io.github.qe7.uis.clickGui.impl.panel.button.component.EnumComponent;
import io.github.qe7.uis.clickGui.impl.panel.button.component.IntComponent;
import io.github.qe7.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Button {

    private final List<io.github.qe7.uis.clickGui.api.types.Component> components = new ArrayList<>();

    private final Module module;

    private float positionX, positionY;

    public int width = 112, height = 14;

    private float totalHeight = 0;

    private boolean extended;

    public Button(Module module) {
        this.module = module;

        for (Setting<?> setting : Osiris.getInstance().getModuleManager().getSettingsByFeature(module)) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanComponent((BooleanSetting) setting));
            }
            if (setting instanceof EnumSetting) {
                components.add(new EnumComponent((EnumSetting<?>) setting));
            }
            if (setting instanceof IntSetting) {
                components.add(new IntComponent((IntSetting) setting));
            }
            if (setting instanceof DoubleSetting) {
                components.add(new DoubleComponent((DoubleSetting) setting));
            }
        }
    }

    public void drawScreen(int par1, int par2, float x, float y) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.positionX = x;
        this.positionY = y;

        Gui.drawRect(positionX, positionY, positionX + width, positionY + height, module.isEnabled() ? HUDModule.getColour(2).getRGB() : new Color(50, 50, 50, 150).getRGB());

        if (MathUtil.isHovered(positionX, positionY, width, height, par1, par2)) {
            Gui.drawRect(positionX, positionY, positionX + width, positionY + height, new Color(0, 0, 0, 100).getRGB());
        }

        fontRenderer.drawStringWithShadow(module.getName(), positionX + 3, positionY + 3, -1);

        if (!Osiris.getInstance().getModuleManager().getSettingsByFeature(this.module).isEmpty()) {
            fontRenderer.drawStringWithShadow(extended ? "-" : "+", positionX + width - 10.5f, positionY + 3.5f, -1);
        }

        this.totalHeight = height;
        if (this.extended) {
            this.totalHeight += 1.0f;
            for (io.github.qe7.uis.clickGui.api.types.Component component : components) {
                if (component.getSetting().shouldHide()) continue;
                component.drawScreen(par1, par2, x + 2, y + totalHeight);
                this.totalHeight += component.getHeight();
            }
        }
    }

    public void mouseClicked(int par1, int par2, int par3) {
        switch (par3) {
            case 0:
                if (this.isHovering(par1, par2)) {
                    this.module.setEnabled(!this.module.isEnabled());
                    return;
                }
                break;
            case 1:
                if (this.isHovering(par1, par2)) {
                    if (Osiris.getInstance().getModuleManager().getSettingsByFeature(this.module).isEmpty()) {
                        this.extended = false;
                        return;
                    }
                    this.extended = !this.extended;
                    return;
                }
                break;
        }

        if (this.extended) {
            for (io.github.qe7.uis.clickGui.api.types.Component component : components) {
                if (component.getSetting().shouldHide()) continue;
                component.mouseClicked(par1, par2, par3);
            }
        }
    }

    public void mouseReleased(int par1, int par2, int par3) {
        if (this.extended) {
            for (io.github.qe7.uis.clickGui.api.types.Component component : components) {
                if (component.getSetting().shouldHide()) continue;
                component.mouseReleased(par1, par2, par3);
            }
        }
    }

    public void keyTyped(char par1, int par2) {
        if (this.extended) {
            for (Component component : components) {
                if (component.getSetting().shouldHide()) continue;
                component.keyTyped(par1, par2);
            }
        }
    }

    public float getHeight() {
        return totalHeight;
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.positionX && mouseX <= this.positionX + this.width && mouseY >= this.positionY && mouseY <= this.positionY + this.height;
    }
}