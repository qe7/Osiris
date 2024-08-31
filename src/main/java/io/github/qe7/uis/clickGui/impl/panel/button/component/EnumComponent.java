package io.github.qe7.uis.clickGui.impl.panel.button.component;

import io.github.qe7.uis.clickGui.api.types.Component;
import io.github.qe7.features.impl.modules.api.settings.impl.EnumSetting;
import io.github.qe7.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import java.awt.*;

import org.lwjgl.input.Mouse;

public class EnumComponent extends Component {

    private final EnumSetting<?> setting;

    private float positionX, positionY;

    public int width = 108, height = 14;

    public EnumComponent(EnumSetting<?> setting) {
        super(setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.positionX = x;
        this.positionY = y;

        if(Mouse.hasWheel() && MathUtil.isHovered(x, y, this.width, this.height, mouseX, mouseY)) {
       	 	int dWheelVariable = Mouse.getDWheel();
            if (dWheelVariable < 0) {
            	this.decrement(this.setting);
            } else if (dWheelVariable > 0){
            	this.increment(this.setting);
            }
        }
        if (MathUtil.isHovered(x, y, this.width, this.height, mouseX, mouseY)) {
            Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        }

        fontRenderer.drawStringWithShadow(this.setting.getName(), x + 3, y + 3, -1);
        fontRenderer.drawStringWithShadow("\u00A77" + this.setting.getValue().getName(), x + 3 + fontRenderer.getStringWidth(this.setting.getName() + " "), y + 3, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int k) {
        if (isHovering(mouseX, mouseY, this.positionX, this.positionY)) {
            switch (k) {
                case 0: {
                    this.increment(this.setting);
                    break;
                }
                case 1: {
                    this.decrement(this.setting);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int k) {}

    @Override
    public void keyTyped(char c, int i) {}

    @Override
    public int getHeight() {
        return this.height;
    }

    private void increment(EnumSetting<?> setting) {
        final int index = setting.getValues().indexOf(setting.getValue());
        if (index + 1 < setting.getValues().size()) {
            setting.setIndex(index + 1);
        } else {
            setting.setIndex(0);
        }
    }

    private void decrement(EnumSetting<?> setting) {
        final int index = setting.getValues().indexOf(setting.getValue());
        if (index - 1 >= 0) {
            setting.setIndex(index - 1);
        } else {
            setting.setIndex(setting.getValues().size() - 1);
        }
    }
}
