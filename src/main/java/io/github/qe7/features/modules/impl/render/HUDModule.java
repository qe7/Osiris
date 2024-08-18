package io.github.qe7.features.modules.impl.render;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.IntSetting;
import io.github.qe7.utils.render.ColourUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiChat;
import net.minecraft.src.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HUDModule extends Module {

    /* watermark */
    private final BooleanSetting watermark = new BooleanSetting("Watermark", true);
    private final BooleanSetting showVersion = new BooleanSetting("Show Build Version", true).supplyIf(watermark::getValue);
    private final BooleanSetting showMCVersion = new BooleanSetting("Show MC Version", true).supplyIf(watermark::getValue);

    /* modules */
    private final BooleanSetting modules = new BooleanSetting("Modules", true);
    private final BooleanSetting modulesSuffix = new BooleanSetting("Suffix", true).supplyIf(this.modules::getValue);

    /* bottom right */
    private final BooleanSetting coordinates = new BooleanSetting("Coordinates", true);
    private final BooleanSetting netherCoordinates = new BooleanSetting("Nether Coordinates", true).supplyIf(this.coordinates::getValue);

    /* bottom left */
    private final BooleanSetting durability = new BooleanSetting("Durability", true);
    private final BooleanSetting fps = new BooleanSetting("FPS", true);

    /* misc */
    private final BooleanSetting welcomer = new BooleanSetting("Welcomer", true);

    /* theme color */
    public static final IntSetting red = new IntSetting("Red", 255, 0, 255, 1);
    public static final IntSetting green = new IntSetting("Green", 255, 0, 255, 1);
    public static final IntSetting blue = new IntSetting("Blue", 255, 0, 255, 1);

    private final List<Long> fpsCounter = new ArrayList<>();

    public HUDModule() {
        super("HUD", "Displays an interface that holds important client information", ModuleCategory.RENDER);

        this.setEnabled(true);
    }

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenListener = event -> {
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        int bottomRightOffset = (Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 14 : 0);
        int bottomLeftOffset = (Minecraft.getMinecraft().currentScreen instanceof GuiChat ? 14 : 0);

        if (this.watermark.getValue()) {
            String display = Osiris.getInstance().getName() + "§7";

            if (showMCVersion.getValue()) {
                display += " (rel 1.2.5)";
            }

            if (showVersion.getValue()) {
                display += " v" + Osiris.getInstance().getVersion();
            }

            fontRenderer.drawStringWithShadow(display, 2, 2, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        }

        if (this.coordinates.getValue()) {
            if (this.netherCoordinates.getValue()) {
                // get the deminsion the player is in (0 = overworld, -1 = nether, 1 = end)
                final int dimension = Minecraft.getMinecraft().thePlayer.dimension;

                // create overworld coordinates (if in nether * 8)
                double x = Minecraft.getMinecraft().thePlayer.posX * (dimension == -1 ? 8 : 1);
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ * (dimension == -1 ? 8 : 1);

                x = Math.round(x * 10.0) / 10.0;
                y = Math.round(y * 10.0) / 10.0;
                z = Math.round(z * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("Overworld§7 %.1f %.1f %.1f", x, y, z), 2, scaledResolution.getScaledHeight() - bottomLeftOffset - 10, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());

                // create nether coordinates (if in overworld / 8)
                double netherX = Minecraft.getMinecraft().thePlayer.posX / (dimension == 0 ? 8 : 1);
                double netherZ = Minecraft.getMinecraft().thePlayer.posZ / (dimension == 0 ? 8 : 1);

                netherX = Math.round(netherX * 10.0) / 10.0;
                netherZ = Math.round(netherZ * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("Nether§7 %.1f %.1f", netherX, netherZ), 2, scaledResolution.getScaledHeight() - bottomLeftOffset - 20, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
            } else {
                double x = Minecraft.getMinecraft().thePlayer.posX;
                double y = Minecraft.getMinecraft().thePlayer.posY;
                double z = Minecraft.getMinecraft().thePlayer.posZ;

                x = Math.round(x * 10.0) / 10.0;
                y = Math.round(y * 10.0) / 10.0;
                z = Math.round(z * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("XYZ§7 %.1f %.1f %.1f", x, y, z), 2, scaledResolution.getScaledHeight() - bottomLeftOffset - 10, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
            }
        }

        if (this.durability.getValue()) {
            if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getMaxDamage() != 0) {
                final int x = scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth("Durability " + (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getMaxDamage() - Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItemDamage()));
                final int y = scaledResolution.getScaledHeight() - bottomRightOffset - 10;

                fontRenderer.drawStringWithShadow("Durability §7" + (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getMaxDamage() - Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItemDamage()), x, y, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
                bottomRightOffset += 10;
            }
        }

        if (this.fps.getValue()) {
            fpsCounter.add(System.currentTimeMillis());

            fpsCounter.removeIf(time -> System.currentTimeMillis() - time > 1000);

            final int x = scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth("FPS " + fpsCounter.size());
            final int y = scaledResolution.getScaledHeight() - bottomRightOffset - 10;

            fontRenderer.drawStringWithShadow("FPS §7" + fpsCounter.size(), x, y, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
            bottomRightOffset += 10;
        }

        if (this.modules.getValue()) {
            final List<Module> modules = Osiris.getInstance().getModuleManager().getMap().values().stream().filter(Module::isEnabled).collect(Collectors.toList());

            int y = 2;
            for (Module module : modules) {
                final String name = module.getName();
                final String suffix = module.getSuffix();

                final float x = scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(name) - 2 - (suffix != null && this.modulesSuffix.getValue() ? fontRenderer.getStringWidth(suffix) : 0);

                fontRenderer.drawStringWithShadow(name, x - (suffix != null && this.modulesSuffix.getValue() ? 4 : 0), y, ColourUtility.getMixedColor(new Color(red.getValue(), green.getValue(), blue.getValue()), new Color(red.getValue(), green.getValue(), blue.getValue()).darker(), y * 10).getRGB());

                if (suffix != null && this.modulesSuffix.getValue()) {
                    fontRenderer.drawStringWithShadow("§7" + suffix, x + fontRenderer.getStringWidth(name), y, -1);
                }

                y += 10;
            }
        }

        if (welcomer.getValue()) {
            String display = getWelcomeMessage();

            fontRenderer.drawStringWithShadow(display + " §7" + Minecraft.getMinecraft().thePlayer.username + "§r!", (float) (scaledResolution.getScaledWidth() / 2 - fontRenderer.getStringWidth(display + " §7" + Minecraft.getMinecraft().thePlayer.username + "§r!") / 2), (float) 5, new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        }
    };

    private static String getWelcomeMessage() {
        String display;

        // depending on the time of day (real world), display a different message (morning, afternoon, evening, night)
        final int hour = java.time.LocalTime.now().getHour();

        if (hour >= 6 && hour < 12) {
            display = "Good morning";
        } else if (hour >= 12 && hour < 18) {
            display = "Good afternoon";
        } else if (hour >= 18) {
            display = "Good evening";
        } else {
            display = "Good night";
        }
        return display;
    }
}
