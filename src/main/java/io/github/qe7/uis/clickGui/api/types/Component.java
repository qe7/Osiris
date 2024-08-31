package io.github.qe7.uis.clickGui.api.types;

import io.github.qe7.features.impl.modules.api.settings.api.Setting;
import io.github.qe7.utils.math.MathUtil;
import lombok.Getter;

@Getter
public abstract class Component {

    private final Setting<?> setting;

    public Component(Setting<?> setting) {
        this.setting = setting;
    }

    public abstract void drawScreen(int mouseX, int mouseY, float x, float y);

    public abstract void mouseClicked(int mouseX, int mouseY, int k);

    public abstract void mouseReleased(int mouseX, int mouseY, int k);

    public abstract void keyTyped(char c, int i);

    public abstract int getHeight();

    public boolean isHovering(int mouseX, int mouseY, float x, float y) {
        return MathUtil.isHovered(x, y, 106, 14, mouseX, mouseY);
    }
}
