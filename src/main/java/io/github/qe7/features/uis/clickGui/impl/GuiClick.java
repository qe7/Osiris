package io.github.qe7.features.uis.clickGui.impl;

import io.github.qe7.features.uis.clickGui.impl.panel.Panel;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.src.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class GuiClick extends GuiScreen {

    private final List<Panel> panels = new ArrayList<>();

    public GuiClick() {
        final float y = 20;
        float x = (960 / 2) - (ModuleCategory.values().length * (112 + 5) / 2);

        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            Panel panel = new Panel(moduleCategory, x, y);
            panels.add(panel);
            x += panel.width + 5;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);

        for (Panel panel : panels) {
            panel.drawScreen(par1, par2, par3);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        for (Panel panel : panels) {
            panel.mouseClicked(par1, par2, par3);
        }
    }

    @Override
    protected void mouseReleased(int par1, int par2, int par3) {
        super.mouseReleased(par1, par2, par3);

        for (Panel panel : panels) {
            panel.mouseReleased(par1, par2, par3);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);

        for (Panel panel : panels) {
            panel.keyTyped(par1, par2);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
