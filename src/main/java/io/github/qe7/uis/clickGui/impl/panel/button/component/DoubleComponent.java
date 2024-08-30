package io.github.qe7.uis.clickGui.impl.panel.button.component;

import io.github.qe7.uis.clickGui.api.types.Component;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.features.modules.impl.render.HUDModule;
import io.github.qe7.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import java.awt.*;

import org.lwjgl.input.Mouse;

public class DoubleComponent extends Component {

    private final DoubleSetting setting;

    private final int width = 108, height = 14;

    private float x, y;

    private boolean dragging;

    public DoubleComponent(DoubleSetting setting) {
        super(setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.x = x;
        this.y = y;

        final double min = this.setting.getMinimum();
        final double max = this.setting.getMaximum();
        final double value = this.setting.getValue();

        final double percent = (value - min) / (max - min);

        if(Mouse.hasWheel() && MathUtil.isHovered(x, y, this.width, this.height, mouseX, mouseY)) {
        	int dWheelVariable = Mouse.getDWheel();
            if (dWheelVariable < 0 && this.setting.getValue() > this.setting.getMinimum()) {
            	 this.setting.setValue(this.setting.getValue() - this.setting.getStep());
            } else if (dWheelVariable > 0 && this.setting.getValue() < this.setting.getMaximum()){
            	 this.setting.setValue(this.setting.getValue() + this.setting.getStep());
           }
        }
        
        if (this.dragging) {
            final double diff = mouseX - x;
            double newValue = min + (diff / width) * (max - min);
            if (newValue < min) newValue = min;
            if (newValue > max) newValue = max;
            this.setting.setValue(MathUtil.doStep(newValue, this.setting.getStep(), this.setting.getMinimum(), this.setting.getMaximum()));
        }

        if (MathUtil.isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 100).getRGB());
        }

        Gui.drawRect(this.x, this.y + this.height - 2, this.x + (int) (this.width * percent), this.y + this.height, new Color(HUDModule.getColour(2).getRed(), HUDModule.getColour(2).getGreen(), HUDModule.getColour(2).getBlue(), 150).getRGB());

        fontRenderer.drawStringWithShadow(this.setting.getName(), x + 2, y + 3, new Color(255, 255, 255).getRGB());

        String valueString = String.format("%.2f", this.setting.getValue());

        // remove extra 0s after the decimal point, but only if the value is not an integer
        while (valueString.endsWith("0") && !valueString.endsWith(".0")) {
            valueString = valueString.substring(0, valueString.length() - 1);
        }

        fontRenderer.drawStringWithShadow(valueString, x + width - 2 - fontRenderer.getStringWidth(valueString), y + 3, new Color(255, 255, 255).getRGB());
    }

    @Override
    public void keyTyped(char c, int i) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int k) {
        if (MathUtil.isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            if (k == 0) {
                this.dragging = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int k) {
        if (this.dragging) this.dragging = false;
    }

    public int getHeight() {
        return this.height;
    }
}
