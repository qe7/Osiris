package io.github.qe7;

import io.github.qe7.events.api.EventBus;
import io.github.qe7.managers.impl.CommandManager;
import io.github.qe7.managers.impl.FriendManager;
import io.github.qe7.managers.impl.ModuleManager;
import io.github.qe7.managers.impl.WaypointManager;
import io.github.qe7.utils.configs.FileUtility;
import net.minecraft.client.Minecraft;

/**
 * Main class for the Osiris client
 */
public final class Osiris {

    // Singleton instance
    private static final Osiris instance = new Osiris();

    // Name and version of the client
    private final String name, version;

    // Event bus
    private final EventBus eventBus;

    // Managers
    private final WaypointManager waypointManager;
    private final FriendManager friendManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    // Private constructor
    private Osiris() {
        System.out.println("Osiris instance created!");

        // set name and version
        this.name = "Osiris";
        this.version = "1.0";

        // create event bus
        this.eventBus = new EventBus();

        // create instance of managers
        this.waypointManager = new WaypointManager();
        this.friendManager = new FriendManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
    }

    /**
     * Initialises the client
     */
    public void initialise() {
        System.out.println("Osiris initialising!");

        // create directory for config files
        FileUtility.createDirectory();

        // initialise managers
        this.getWaypointManager().initialise();
        this.getFriendManager().initialise();
        this.getModuleManager().initialise();
        this.getCommandManager().initialise();

        // register shutdown hook, save configs on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Osiris shutting down!");

            this.getFriendManager().saveFriends();
            this.getWaypointManager().saveWaypoints();
            this.getModuleManager().saveModules();

            System.out.println("Osiris shut down!");
        }));

        System.out.println("Osiris initialised!");
    }

    /* Getters */
    public static Osiris getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public WaypointManager getWaypointManager() {
        return waypointManager;
    }

    public FriendManager getFriendManager() {
        return friendManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
