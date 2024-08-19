package io.github.qe7;

import io.github.qe7.events.api.EventBus;
import io.github.qe7.managers.impl.*;
import io.github.qe7.utils.configs.FileUtility;

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
    private final AccountManager accountManager;
    private final WaypointManager waypointManager;
    private final RelationManager relationManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    // Private constructor
    private Osiris() {
        System.out.println("Osiris instance created!");

        // set name and version
        this.name = "Osiris";
        this.version = "1.0.0";

        // create event bus
        this.eventBus = new EventBus();

        // create instance of managers
        this.accountManager = new AccountManager();
        this.waypointManager = new WaypointManager();
        this.relationManager = new RelationManager();
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
        this.getAccountManager().initialise();
        this.getWaypointManager().initialise();
        this.getRelationManager().initialise();
        this.getModuleManager().initialise();
        this.getCommandManager().initialise();

        // register shutdown hook, save configs on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Osiris shutting down!");

            this.getAccountManager().saveAccounts();
            this.getRelationManager().saveRelations();
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

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public WaypointManager getWaypointManager() {
        return waypointManager;
    }

    public RelationManager getRelationManager() {
        return relationManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
