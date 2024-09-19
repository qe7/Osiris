package io.github.qe7;

import io.github.qe7.managers.impl.*;
import io.github.qe7.utils.configs.FileUtil;
import lombok.Getter;
import lunatrius.schematica.SchematicaLoader;
import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;

/**
 * Main class for the client
 */
@Getter
public final class Osiris {

    @Getter
    private static final Osiris instance = new Osiris();

    private final String name, version;

    private final EventBus eventBus = EventManager.builder().setName("Noble").build();

    private final AccountManager accountManager;
    private final WaypointManager waypointManager;
    private final RelationManager relationManager;
    private final ModuleManager moduleManager;
    private final CommandManager commandManager;
    private final PanelManager panelManager;
    
    // Private constructor, to prevent instantiation
    private Osiris() {
        System.out.println("Osiris instance created!");

        this.name = "Osiris";
        this.version = "1.0.6";

        this.accountManager = new AccountManager();
        this.waypointManager = new WaypointManager();
        this.relationManager = new RelationManager();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.panelManager = new PanelManager();
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
        this.getPanelManager().initialise();
        SchematicaLoader.load();
        
        // register shutdown hook, save configs on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        System.out.println("Osiris initialised!");
    }

    public void shutdown() {
        System.out.println("Osiris shutting down!");

        this.getAccountManager().saveAccounts();
        this.getRelationManager().saveRelations();
        this.getWaypointManager().saveWaypoints();
        this.getModuleManager().saveModules();
        this.getPanelManager().savePanels();

        System.out.println("Osiris shut down!");
    }
}