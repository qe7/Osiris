package io.github.qe7.features.clickGui.api.types;

import io.github.qe7.features.modules.api.settings.api.Setting;

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

    public Setting<?> getSetting() {
        return setting;
    }

    public boolean isHovering(int mouseX, int mouseY, float x, float y) {
        return mouseX >= x && mouseX <= x + 106 && mouseY >= y && mouseY <= y + 14;
    }
}
