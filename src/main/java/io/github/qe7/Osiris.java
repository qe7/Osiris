package io.github.qe7;

import io.github.qe7.events.api.EventBus;
import io.github.qe7.managers.impl.*;
import io.github.qe7.utils.configs.FileUtil;

/**
 * Main class for the client
 */
public final class Osiris {

    private static final Osiris instance = new Osiris();

    private final String name, version;

    private final EventBus eventBus;

    private final AccountManager accountManager;
    private final WaypointManager waypointManager;
    private final RelationManager relationManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;

    // Private constructor, to prevent instantiation
    private Osiris() {
        System.out.println("Osiris instance created!");

        this.name = "Osiris";
        this.version = "1.0.3";

        this.eventBus = new EventBus();

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

        FileUtil.createDirectory();

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
