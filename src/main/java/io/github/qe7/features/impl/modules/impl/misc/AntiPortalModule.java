package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;

public class AntiPortalModule extends Module {

    public AntiPortalModule() {
        super("Anti Portal", "Makes it so you can't enter portals?", ModuleCategory.MISC);
    }
}
