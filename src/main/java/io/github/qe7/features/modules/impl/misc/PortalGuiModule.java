package io.github.qe7.features.modules.impl.misc;

import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;

public class PortalGuiModule extends Module {

    public PortalGuiModule() {
        super("Portal GUI", "Makes it so you can open GUI in portal?", ModuleCategory.MISC);
    }
}
