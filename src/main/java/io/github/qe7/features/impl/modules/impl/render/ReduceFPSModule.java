package io.github.qe7.features.impl.modules.impl.render;

import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.IntSetting;

public class ReduceFPSModule extends Module {
	private final IntSetting fpt = new IntSetting("FramesPerTick", 1, 1, 3, 1);

	public ReduceFPSModule() {
		super("ReduceFPS", "Reduces FPS (FPT * 20 = fps)", ModuleCategory.RENDER);
	}

	public int getFpt() {
		return this.fpt.getValue();
	}
}
