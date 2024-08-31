package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.render.RenderScreenEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.IntSetting;
import io.github.qe7.utils.render.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiChat;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HUDModule extends Module {

    private final BooleanSetting watermark = new BooleanSetting("Watermark", true);
    private final BooleanSetting showVersion = new BooleanSetting("Show Build Version", true).supplyIf(watermark::getValue);
    private final BooleanSetting showMCVersion = new BooleanSetting("Show MC Version", true).supplyIf(watermark::getValue);

    private final BooleanSetting modules = new BooleanSetting("Modules", true);
    private final BooleanSetting modulesSuffix = new BooleanSetting("Suffix", true).supplyIf(this.modules::getValue);

    private final BooleanSetting direction = new BooleanSetting("Direction", true);
    private final BooleanSetting coordinates = new BooleanSetting("Coordinates", true);
    private final BooleanSetting netherCoordinates = new BooleanSetting("Nether Coordinates", true).supplyIf(this.coordinates::getValue);

    private final BooleanSetting durability = new BooleanSetting("Durability", true);
    private final BooleanSetting fps = new BooleanSetting("FPS", true);

    private final BooleanSetting welcomer = new BooleanSetting("Welcomer", true);

    private static final BooleanSetting rainbow = new BooleanSetting("Rainbow", false);

    private static final IntSetting rainbowSpeed = new IntSetting("Speed", 5000, 100, 10000, 100).supplyIf(rainbow::getValue);
    private static final DoubleSetting rainbowSaturation = new DoubleSetting("Saturation", 1.0, 0.1, 1.0, 0.1).supplyIf(rainbow::getValue);
    private static final DoubleSetting rainbowBrightness = new DoubleSetting("Brightness", 1.0, 0.1, 1.0, 0.1).supplyIf(rainbow::getValue);

    private static final IntSetting red = new IntSetting("Red", 255, 0, 255, 1).supplyIf(() -> !rainbow.getValue());
    private static final IntSetting green = new IntSetting("Green", 255, 0, 255, 1).supplyIf(() -> !rainbow.getValue());
    private static final IntSetting blue = new IntSetting("Blue", 255, 0, 255, 1).supplyIf(() -> !rainbow.getValue());

    private final Minecraft mc = Minecraft.getMinecraft();

    private final List<Long> fpsCounter = new ArrayList<>();

    public HUDModule() {
        super("HUD", "Displays an interface that holds important client information", ModuleCategory.RENDER);
    }

    @EventLink
    public final Listener<RenderScreenEvent> renderScreenListener = event -> {
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final FontRenderer fontRenderer = mc.fontRenderer;

        int bottomRightOffset = (mc.currentScreen instanceof GuiChat ? 24 : 10);
        int bottomLeftOffset = (mc.currentScreen instanceof GuiChat ? 24 : 10);

        if (this.watermark.getValue()) {
            String display = Osiris.getInstance().getName() + "§7";

            if (showMCVersion.getValue()) {
                display += " (rel 1.2.5)";
            }

            if (showVersion.getValue()) {
                display += " v" + Osiris.getInstance().getVersion();
            }

            fontRenderer.drawStringWithShadow(display, 2, 2, getColour(2).getRGB());
        }

        if (this.coordinates.getValue()) {
            if (this.netherCoordinates.getValue()) {
                // get the dimension the player is in (0 = overworld, -1 = nether, 1 = end)
                final int dimension = mc.thePlayer.dimension;

                // create overworld coordinates (if in nether * 8)
                double x = mc.thePlayer.posX * (dimension == -1 ? 8 : 1);
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ * (dimension == -1 ? 8 : 1);

                x = Math.round(x * 10.0) / 10.0;
                y = Math.round(y * 10.0) / 10.0;
                z = Math.round(z * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("Overworld§7 %.1f %.1f %.1f", x, y, z), 2, scaledResolution.getScaledHeight() - bottomLeftOffset, getColour(bottomLeftOffset).getRGB());
                bottomLeftOffset += 10;

                // create nether coordinates (if in overworld / 8)
                double netherX = mc.thePlayer.posX / (dimension == 0 ? 8 : 1);
                double netherZ = mc.thePlayer.posZ / (dimension == 0 ? 8 : 1);

                netherX = Math.round(netherX * 10.0) / 10.0;
                netherZ = Math.round(netherZ * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("Nether§7 %.1f %.1f", netherX, netherZ), 2, scaledResolution.getScaledHeight() - bottomLeftOffset, getColour(bottomLeftOffset).getRGB());
            } else {
                double x = mc.thePlayer.posX;
                double y = mc.thePlayer.posY;
                double z = mc.thePlayer.posZ;

                x = Math.round(x * 10.0) / 10.0;
                y = Math.round(y * 10.0) / 10.0;
                z = Math.round(z * 10.0) / 10.0;

                fontRenderer.drawStringWithShadow(String.format("XYZ§7 %.1f %.1f %.1f", x, y, z), 2, scaledResolution.getScaledHeight() - bottomLeftOffset - 10, getColour(bottomLeftOffset).getRGB());
            }
            bottomLeftOffset += 10;
        }

        if(this.direction.getValue()) {
        	String direction = "";
        	switch(MathHelper.floor_double((double) ((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3) {
        		case 0:
        			direction = "South, Z+";
        			break;
        		case 1:
        			direction = "West,  X-";
        			break;
        		case 2:
        			direction = "North, Z-";
        			break;
        		case 3:
        			direction = "East,  X+";
        			break;
        	}
        	fontRenderer.drawStringWithShadow("Direction: §7" + direction, 2, scaledResolution.getScaledHeight() - bottomLeftOffset, getColour(bottomLeftOffset).getRGB());
            bottomLeftOffset += 10;
        }

        if (this.durability.getValue()) {
            if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.inventory.getCurrentItem().getMaxDamage() != 0) {
                final int x = scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth("Durability " + (mc.thePlayer.inventory.getCurrentItem().getMaxDamage() - mc.thePlayer.inventory.getCurrentItem().getItemDamage()));
                final int x2 = scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth(String.valueOf((mc.thePlayer.inventory.getCurrentItem().getMaxDamage() - mc.thePlayer.inventory.getCurrentItem().getItemDamage())));
                final int y = scaledResolution.getScaledHeight() - bottomRightOffset;

                fontRenderer.drawStringWithShadow("Durability ", x, y, getColour(bottomRightOffset).getRGB());
                fontRenderer.drawStringWithShadow("" + (mc.thePlayer.inventory.getCurrentItem().getMaxDamage() - mc.thePlayer.inventory.getCurrentItem().getItemDamage()), x2, y, getDurabilityColour(mc.thePlayer.inventory.getCurrentItem().getMaxDamage() - mc.thePlayer.inventory.getCurrentItem().getItemDamage(), mc.thePlayer.inventory.getCurrentItem().getMaxDamage()).getRGB());
                bottomRightOffset += 10;
            }
        }

        if (this.fps.getValue()) {
            fpsCounter.add(System.currentTimeMillis());

            fpsCounter.removeIf(time -> System.currentTimeMillis() - time > 1000);

            final int x = scaledResolution.getScaledWidth() - 2 - fontRenderer.getStringWidth("FPS " + fpsCounter.size());
            final int y = scaledResolution.getScaledHeight() - bottomRightOffset;

            fontRenderer.drawStringWithShadow("FPS §7" + fpsCounter.size(), x, y, getColour(bottomRightOffset).getRGB());
        }

        if (this.modules.getValue()) {
            final List<Module> modules = Osiris.getInstance().getModuleManager().getMap().values().stream().filter(Module::isEnabled).sorted(
                    (module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName())
            ).collect(Collectors.toList());

            int y = 2;
            for (Module module : modules) {
                final String name = module.getName();
                final String suffix = module.getSuffix();

                final float x = scaledResolution.getScaledWidth() - fontRenderer.getStringWidth(name) - 2 - (suffix != null && this.modulesSuffix.getValue() ? fontRenderer.getStringWidth(suffix) : 0);

                fontRenderer.drawStringWithShadow(name, x - (suffix != null && this.modulesSuffix.getValue() ? 4 : 0), y, getColour(y).getRGB());

                if (suffix != null && this.modulesSuffix.getValue()) {
                    fontRenderer.drawStringWithShadow("§7" + suffix, x + fontRenderer.getStringWidth(name), y, -1);
                }

                y += 10;
            }
        }

        if (welcomer.getValue()) {
            String display = getWelcomeMessage();

            fontRenderer.drawStringWithShadow(display + " §7" + mc.thePlayer.username + "§r!", (float) (scaledResolution.getScaledWidth() / 2 - fontRenderer.getStringWidth(display + " §7" + mc.thePlayer.username + "§r!") / 2), (float) 5, getColour(5).getRGB());
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

    private Color getDurabilityColour(final int currentDurability, final int maxDurability) {
        final int durability = (int) ((double) currentDurability / (double) maxDurability * 100);

        if (durability >= 75) {
            return new Color(0, 255, 0);
        } else if (durability >= 50) {
            return new Color(255, 255, 0);
        } else if (durability >= 25) {
            return new Color(255, 165, 0);
        } else {
            return new Color(255, 0, 0);
        }
    }

    public static Color getColour(final int offset) {
        if (rainbow.getValue()) {
            return new Color(ColourUtil.getRainbow(rainbowSpeed.getValue(), rainbowSaturation.getValue().floatValue(), rainbowBrightness.getValue().floatValue(), offset * 10 /* offset by 10 to make more noticeable */));
        } else {
            return new Color(red.getValue(), green.getValue(), blue.getValue());
        }
    }
}
