package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.uis.clickGui.impl.panel.Panel;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.configs.FileUtil;

public final class PanelManager extends Manager<ModuleCategory, Panel> {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public void initialise() {
		float y = 20;
        final float x = 20;

        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
        	if(!getMap().containsKey(moduleCategory)) {
        		System.out.println(x + "x " + y + "y panel registered " + moduleCategory.getName());
        		Panel panel = new Panel(moduleCategory, x, y);
            	getMap().putIfAbsent(moduleCategory, panel);
        	}
            y += 19;
        }

		this.loadPanels();

		Osiris.getInstance().getEventBus().register(this);
		System.out.println("PanelManager initialised!");
	}

	public void register(Panel panel) {
        try {
        	ModuleCategory panelName = panel.getModuleCategory();
            getMap().putIfAbsent(panelName, panel);
        } catch (Exception e) {
            System.out.println("Failed to register panel: " + panel.getPanelName() + " - " + e.getMessage());
        }
    }

	public void savePanels() {
        JsonObject jsonObject = new JsonObject();

        for (Panel panel : getMap().values()) {
            jsonObject.add(panel.getPanelName(), panel.serialize());
        }

        FileUtil.writeFile("panels", GSON.toJson(jsonObject));
    }

    public void loadPanels() {
        String config = FileUtil.readFile("panels");

        if (config == null) {
            return;
        }

        JsonObject jsonObject = GSON.fromJson(config, JsonObject.class);

        for (Panel panel : getMap().values()) {
            if (jsonObject.has(panel.getPanelName())) {
                try {
                    panel.deserialize(jsonObject.getAsJsonObject(panel.getPanelName()));
                } catch (Exception e) {
                    System.out.println("Failed to load config for panel: " + panel.getPanelName() + " - " + e.getMessage());
                    return;
                }
            }
        }
    }
}
