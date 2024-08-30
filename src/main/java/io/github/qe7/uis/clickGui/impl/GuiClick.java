package io.github.qe7.uis.clickGui.impl;

import io.github.qe7.uis.clickGui.impl.panel.Panel;
import io.github.qe7.Osiris;
import net.minecraft.src.GuiScreen;

public class GuiClick extends GuiScreen {

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);

        for (Panel panel : Osiris.getInstance().getPanelManager().getMap().values()) {
            panel.drawScreen(par1, par2);
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);

        for (Panel panel : Osiris.getInstance().getPanelManager().getMap().values()) {
            panel.mouseClicked(par1, par2, par3);
        }
    }

    @Override
    protected void mouseReleased(int par1, int par2, int par3) {
        super.mouseReleased(par1, par2, par3);

        for (Panel panel : Osiris.getInstance().getPanelManager().getMap().values()) {
            panel.mouseReleased(par1, par2, par3);
        }
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);

        for (Panel panel : Osiris.getInstance().getPanelManager().getMap().values()) {
            panel.keyTyped(par1, par2);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
