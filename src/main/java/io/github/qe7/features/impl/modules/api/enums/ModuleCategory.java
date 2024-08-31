package io.github.qe7.features.impl.modules.api.enums;

import lombok.Getter;

import java.awt.*;

@Getter
public enum ModuleCategory {
    COMBAT("Combat", new Color(185, 120, 148)),
    MOVEMENT("Movement", new Color(120, 185, 148)),
    MISC("Miscellaneous", new Color(148, 148, 185)),
    RENDER("Render", new Color(185, 148, 120)),
    CHAT("Chat", new Color(185, 148, 185)),
    EXPLOIT("Exploit", new Color(148, 185, 120));

    private final String name;
    private final Color color;

    ModuleCategory(final String name, final Color color) {
        this.name = name;
        this.color = color;
    }
}
