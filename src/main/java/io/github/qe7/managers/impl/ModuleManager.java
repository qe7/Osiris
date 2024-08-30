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
import io.github.qe7.features.modules.impl.chat.*;
import io.github.qe7.features.modules.impl.combat.CriticalsModule;
import io.github.qe7.features.modules.impl.combat.KillAuraModule;
import io.github.qe7.features.modules.impl.combat.VelocityModule;
import io.github.qe7.features.modules.impl.exploit.AntiHungerModule;
import io.github.qe7.features.modules.impl.exploit.FastUseModule;
import io.github.qe7.features.modules.impl.exploit.LightningTrackerModule;
import io.github.qe7.features.modules.impl.exploit.RegenModule;
import io.github.qe7.features.modules.impl.misc.*;
import io.github.qe7.features.modules.impl.movement.*;
import io.github.qe7.features.modules.impl.render.*;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.configs.FileUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;

public final class ModuleManager extends Manager<Class<? extends Module>, Module> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Getter
    private final Map<Module, List<Setting<?>>> setting = new HashMap<>();

    public void initialise() {
        List<Module> modules = new ArrayList<>();

        modules.add(new ShortTellModule());
        modules.add(new ChatLoggerModule());
        modules.add(new MuteDMsModule());
        modules.add(new HideChatModule());
        modules.add(new AutoLoginModule());
        modules.add(new GreenTextModule());
        modules.add(new SuffixModule());
        modules.add(new WelcomerModule());

        modules.add(new CriticalsModule());
        modules.add(new KillAuraModule());
        modules.add(new VelocityModule());

        modules.add(new AntiHungerModule());
        modules.add(new FastUseModule());
        modules.add(new LightningTrackerModule());
        modules.add(new RegenModule());

        modules.add(new WorldDLModule());
        modules.add(new AntiPortalModule());
        modules.add(new PortalGuiModule());
        modules.add(new AutoDisconnectModule());
        modules.add(new AutoToolModule());
        modules.add(new AutoWalkModule());
        modules.add(new FastBreakModule());
        modules.add(new FastPlaceModule());
        modules.add(new FreeCamModule());
        modules.add(new GameSpeedModule());
        modules.add(new NoFallDamageModule());
        modules.add(new ScaffoldModule());
        modules.add(new SprintModule());
        modules.add(new YawModule());

        modules.add(new FlyModule());
        modules.add(new InventoryMoveModule());
        modules.add(new NoSlowdownModule());
        modules.add(new SpeedModule());
        modules.add(new StepModule());

        modules.add(new BrightnessModule());
        modules.add(new CameraModule());
        modules.add(new ClickGUIModule());
        modules.add(new ESPModule());
        modules.add(new HUDModule());
        modules.add(new NameTagsModule());
        modules.add(new NoRenderModule());
        modules.add(new TracersModule());
        modules.add(new WorldTimeModule());
        modules.add(new XRayModule());

        modules.forEach(module -> register(module.getClass()));

        this.loadModules();

        Osiris.getInstance().getEventBus().register(this);
        System.out.println("ModuleManager initialised!");
    }

    public void register(Class<? extends Module> type) {
        try {
            Module module = type.newInstance();
            getMap().putIfAbsent(type, module);

            for (Field declaredField : type.getDeclaredFields()) {
                if (declaredField.getType().getSuperclass() == null) continue;
                if (!declaredField.getType().getSuperclass().equals(Setting.class)) continue;

                declaredField.setAccessible(true);

                this.addSetting(this.getMap().get(type), (Setting<?>) declaredField.get(this.getMap().get(type)));
                System.out.println("Registered setting: " + declaredField.getName() + " for module: " + type.getSimpleName());
            }
        } catch (Exception e) {
            System.out.println("Failed to register module: " + type.getSimpleName() + " - " + e.getMessage());
        }
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

        FileUtil.writeFile("modules", GSON.toJson(jsonObject));
    }

    public void loadModules() {
        String config = FileUtil.readFile("modules");

        if (config == null) {
            return;
        }

        JsonObject jsonObject = GSON.fromJson(config, JsonObject.class);

        for (Module module : this.getMap().values()) {
            if (jsonObject.has(module.getName())) {
                try {
                    module.deserialize(jsonObject.getAsJsonObject(module.getName()));
                } catch (Exception e) {
                    System.out.println("Failed to load config for module: " + module.getName() + " - " + e.getMessage());
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
