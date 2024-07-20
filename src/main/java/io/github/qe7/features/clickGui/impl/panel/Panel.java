package io.github.qe7.features.clickGui.impl.panel;

import io.github.qe7.Osiris;
import io.github.qe7.features.clickGui.impl.panel.button.Button;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Panel {

    private final List<Button> buttons = new ArrayList<>();

    private final ModuleCategory moduleCategory;

    private float positionX, positionY, draggingX, getDraggingX;

    public final int width = 112, height = 14;

    private float totalHeight = 0;

    private boolean dragging, extended = true;

    public Panel(ModuleCategory moduleCategory, float positionX, float positionY) {
        this.moduleCategory = moduleCategory;
        this.positionX = positionX;
        this.positionY = positionY;

        for (Module module : Osiris.getInstance().getModuleManager().getMap().values()) {
            if (module.getCategory() == moduleCategory) {
                buttons.add(new Button(module));
            }
        }
    }

    public void drawScreen(int par1, int par2, float par3) {
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        if (dragging) {
            positionX = draggingX + par1;
            positionY = getDraggingX + par2;
        }

        Gui.drawRect(positionX, positionY, positionX + width, positionY + height, new Color(0, 0, 0, 100).getRGB());
        Gui.drawRect(positionX, positionY - 0.5f, positionX + width + 0.5f, positionY, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX, positionY + height, positionX + width + 0.5f, positionY + height + 0.5f, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX - 0.5f, positionY - 0.5f, positionX, positionY + height + 0.5f, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX + width, positionY, positionX + width + 0.5f, positionY + height, new Color(0, 0, 0).getRGB());

        Gui.drawRect(positionX + width - 13, positionY + 2, positionX + width - 3, positionY + 12, new Color(0, 0, 0, 100).getRGB());
        Gui.drawRect(positionX + width - 13 - 0.5f, positionY + 2 - 0.5f, positionX + width - 3 + 0.5f, positionY + 2, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX + width - 13 - 0.5f, positionY + 12, positionX + width - 3 + 0.5f, positionY + 12 + 0.5f, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX + width - 13 - 0.5f, positionY + 2, positionX + width - 13, positionY + 12, new Color(0, 0, 0).getRGB());
        Gui.drawRect(positionX + width - 3, positionY + 2, positionX + width - 3 + 0.5f, positionY + 12, new Color(0, 0, 0).getRGB());

        fontRenderer.drawString(extended ? "-" : "+", positionX + width - 10.5f, positionY + 3.5f, -1);

        fontRenderer.drawStringWithShadow(moduleCategory.getName(), positionX + 3, positionY + 3, -1);

        if (extended) {
            Gui.drawRect(positionX, positionY + height + 0.5f, positionX + width, positionY + totalHeight + 1, new Color(0, 0, 0, 100).getRGB());
            Gui.drawRect(positionX, positionY + totalHeight + 1, positionX + width + 0.5f, positionY + totalHeight + 1.5f, new Color(0, 0, 0).getRGB());
            Gui.drawRect(positionX + width, positionY + height + 0.5f, positionX + width + 0.5f, positionY + totalHeight + 1, new Color(0, 0, 0).getRGB());
            Gui.drawRect(positionX - 0.5f, positionY + height + 0.5f, positionX, positionY + totalHeight + 1 + 0.5f, new Color(0, 0, 0).getRGB());
        }

        totalHeight = height + 2.5f;
        if (extended) {
            for (Button button : buttons) {
                button.drawScreen(par1, par2, positionX + 2, positionY + totalHeight);
                totalHeight += button.getHeight() + 2.f;
            }
        }
    }

    public void mouseClicked(int par1, int par2, int par3) {
        switch (par3) {
            case 0: {
                if (par1 >= positionX && par1 <= positionX + width && par2 >= positionY && par2 <= positionY + 12) {
                    dragging = true;
                    draggingX = positionX - par1;
                    getDraggingX = positionY - par2;
                }
                break;
            }
            case 1: {
                if (par1 >= positionX && par1 <= positionX + width && par2 >= positionY && par2 <= positionY + 12) {
                    extended = !extended;
                }
                break;
            }
        }

        if (extended) {
            for (Button button : buttons) {
                button.mouseClicked(par1, par2, par3);
            }
        }
    }

    public void mouseReleased(int par1, int par2, int par3) {
        if (dragging) {
            dragging = false;
        }

        if (extended) {
            for (Button button : buttons) {
                button.mouseReleased(par1, par2, par3);
            }
        }
    }

    public void keyTyped(char par1, int par2) {
        if (extended) {
            for (Button button : buttons) {
                button.keyTyped(par1, par2);
            }
        }
    }
}
