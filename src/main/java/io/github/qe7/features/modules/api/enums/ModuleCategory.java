package io.github.qe7.features.modules.api.enums;

import java.awt.*;

public enum ModuleCategory {
    COMBAT("Combat", new Color(211, 47, 47)),
    MOVEMENT("Movement", new Color(76, 175, 80)),
    MISC("Miscellaneous", new Color(158, 158, 158)),
    RENDER("Render", new Color(103, 58, 183)),
    EXPLOIT("Exploit", new Color(255, 87, 34));

    private final String name;
    private final Color color;

    ModuleCategory(final String name, final Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return color;
    }
}
