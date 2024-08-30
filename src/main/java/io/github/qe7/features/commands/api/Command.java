package io.github.qe7.features.commands.api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Command {

    private final String name, description;

    @Setter
    private String usage = "No usage provided";

    private final List<String> aliases = new ArrayList<>();

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute(String[] args);
}
