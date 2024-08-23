package io.github.qe7.features.commands.api;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private final String name, description;

    private String usage = "No usage provided";

    private final List<String> aliases = new ArrayList<>();

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

    public List<String> getAliases() {
        return aliases;
    }

    public abstract void execute(String[] args);
}
