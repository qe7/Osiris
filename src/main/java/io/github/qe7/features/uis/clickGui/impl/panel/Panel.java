package io.github.qe7.features.uis.clickGui.impl.panel;

import io.github.qe7.Osiris;
import io.github.qe7.features.uis.clickGui.impl.panel.button.Button;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.impl.render.HUDModule;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class       Panel {

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

        final Color themeColor = new Color(HUDModule.red.getValue(), HUDModule.green.getValue(), HUDModule.blue.getValue());

        Gui.drawRect(positionX - 1, positionY - 1, positionX + width + 1, positionY + height, themeColor.getRGB());
        fontRenderer.drawStringWithShadow(moduleCategory.getName(), positionX + ((float) width / 2 - (float) fontRenderer.getStringWidth(moduleCategory.getName()) / 2), positionY + 2, 0xFFFFFFFF);

        Gui.drawRect(positionX - 2, positionY - 1, positionX - 1, positionY + totalHeight + 1, themeColor.getRGB());
        Gui.drawRect(positionX + width + 1, positionY - 1, positionX + width + 2, positionY + totalHeight + 1, themeColor.getRGB());
        Gui.drawRect(positionX - 2, positionY + totalHeight, positionX + width + 2, positionY + totalHeight + 1, themeColor.getRGB());

        Gui.drawRect(positionX - 1, positionY + height, positionX + width + 1, positionY + totalHeight, new Color(0, 0, 0, 150).getRGB());

        totalHeight = height + 1.0f;
        if (extended) {
            for (Button button : buttons) {
                button.drawScreen(par1, par2, positionX, positionY + totalHeight);
                totalHeight += button.getHeight() + 1.f;
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
