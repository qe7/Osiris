package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.game.KeyInputEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.settings.api.Setting;
import io.github.qe7.features.modules.impl.render.HUDModule;
import io.github.qe7.features.modules.impl.combat.CriticalsModule;
import io.github.qe7.features.modules.impl.combat.KillAuraModule;
import io.github.qe7.features.modules.impl.combat.VelocityModule;
import io.github.qe7.features.modules.impl.exploit.FastUseModule;
import io.github.qe7.features.modules.impl.exploit.RegenModule;
import io.github.qe7.features.modules.impl.misc.*;
import io.github.qe7.features.modules.impl.movement.*;
import io.github.qe7.features.modules.impl.render.*;
import io.github.qe7.managers.api.TypeManager;
import io.github.qe7.utils.configs.FileUtility;

import java.lang.reflect.Field;
import java.util.*;

public final class ModuleManager extends TypeManager<Module> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Class<Module>[] MODULES = new Class[]{
            CriticalsModule.class,
            KillAuraModule.class,
            VelocityModule.class,

            FastUseModule.class,
            RegenModule.class,

            AutoToolModule.class,
            ChatUtilsModule.class,
            FastBreakModule.class,
            FreeCamModule.class,
            SprintModule.class,

            FlyModule.class,
            InventoryMoveModule.class,
            NoSlowdownModule.class,
            SpeedModule.class,
            StepModule.class,

            BrightnessModule.class,
            ClickGUIModule.class,
            ESPModule.class,
            HUDModule.class,
            NameTagsModule.class,
            NoRenderModule.class,
            XRayModule.class,
    };

    private final Map<Module, List<Setting<?>>> setting = new HashMap<>();

    public void initialise() {
        System.out.println("ModuleManager initialising!");

        for (Class<Module> clazz : MODULES) {
            try {
                register(clazz);

                System.out.println("Initialised module: " + clazz.getSimpleName());

                if (getMap().get(clazz).isEnabled()) {
                    getMap().get(clazz).onEnable();
                }

                for (Field declaredField : clazz.getDeclaredFields()) {
                    if (declaredField.getType().getSuperclass() == null) continue;
                    if (!declaredField.getType().getSuperclass().equals(Setting.class)) continue;

                    declaredField.setAccessible(true);

                    this.addSetting(this.getMap().get(clazz), (Setting<?>) declaredField.get(this.getMap().get(clazz)));
                    System.out.println("Initialised setting: " + declaredField.getName() + " for module: " + clazz.getSimpleName());
                }
            } catch (Exception e) {
                System.out.println("Failed to initialise module: " + clazz.getSimpleName() + " - " + e.getMessage());
                throw new RuntimeException("Failed to initialise module: " + clazz.getSimpleName() + " - " + e.getMessage());
            }
        }

        this.loadModules();

        Osiris.getInstance().getEventBus().register(this);
        System.out.println("ModuleManager initialised!");
    }

    public Map<Module, List<Setting<?>>> getSetting() {
        return setting;
    }

    public List<Setting<?>> getSettingsByFeature(Module feature) {
        return setting.getOrDefault(feature, Collections.emptyList());
    }

    public void addSetting(Module feature, Setting<?> property) {
        setting.putIfAbsent(feature, new ArrayList<>());
        setting.get(feature).add(property);
    }

    public void saveModules() {
        JsonObject jsonObject = new JsonObject();

        for (Module module : this.getMap().values()) {
            jsonObject.add(module.getName(), module.serialize());
        }

        FileUtility.writeFile("modules", GSON.toJson(jsonObject));
    }

    public void loadModules() {
        String config = FileUtility.readFile("modules");

        if (config == null) {
            return;
        }

        JsonObject jsonObject = GSON.fromJson(config, JsonObject.class);

        for (Module module : this.getMap().values()) {
            if (jsonObject.has(module.getName())) {
                try {
                    module.deserialize(jsonObject.getAsJsonObject(module.getName()));
                } catch (Exception e) {
                    System.out.println("Failed to load module: " + module.getName() + " - " + e.getMessage());
                }
            }
        }
    }

    @EventLink
    public final Listener<KeyInputEvent> keyInputListener = event -> {
        for (Module module : this.getMap().values()) {
            if (module.getKeyCode() == event.getKeyCode()) {
                module.setEnabled(!module.isEnabled());
            }
        }
    };
}
