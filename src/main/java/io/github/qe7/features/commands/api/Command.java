package io.github.qe7.features.commands.api;

public abstract class Command {

    private final String name, description;

    private String usage = "No usage provided";

    private String[] aliases = new String[0];

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public abstract void execute(String[] args);
}
