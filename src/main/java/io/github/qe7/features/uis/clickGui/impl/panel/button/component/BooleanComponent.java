package io.github.qe7.features.uis.clickGui.impl.panel.button.component;

import io.github.qe7.features.uis.clickGui.api.types.Component;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.impl.render.HUDModule;
import io.github.qe7.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class BooleanComponent extends Component {

    private final BooleanSetting setting;

    private float positionX, positionY;

    public int width = 108, height = 14;

    public BooleanComponent(BooleanSetting setting) {
        super(setting);

        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.positionX = x;
        this.positionY = y;

        if (MathUtil.isHovered(x, y, this.width, this.height, mouseX, mouseY)) {
            if(Mouse.hasWheel()) {
                int dWheelVariable = Mouse.getDWheel();
                if (dWheelVariable < 0) {
                    if(this.setting.getValue()) {
                        this.setting.setValue(false);
                    } else
                        this.setting.setValue(true);
                } else if (dWheelVariable > 0){
                    if(this.setting.getValue()) {
                        this.setting.setValue(false);
                    } else
                        this.setting.setValue(true);
                }
            }

            Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
        }

        if (this.setting.getValue()) {
            Gui.drawRect(x, y, x + width, y + height, new Color(HUDModule.red.getValue(), HUDModule.green.getValue(), HUDModule.blue.getValue(), 150).getRGB());
        }

        fontRenderer.drawStringWithShadow(this.setting.getName(), x + 3, y + 3, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int k) {
        if (k == 0) {
            if (isHovering(mouseX, mouseY, this.positionX, this.positionY)) {
                this.setting.setValue(!this.setting.getValue());
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int k) {

    }

    @Override
    public void keyTyped(char c, int i) {

    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
