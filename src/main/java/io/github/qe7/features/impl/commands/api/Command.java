package io.github.qe7.features.impl.commands.api;

import io.github.qe7.features.api.Feature;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Command extends Feature {

    @Setter
    private String usage = "No usage provided";

    private final List<String> aliases = new ArrayList<>();

    public Command(String name, String description) {
        super(name, description);
    }

    public abstract void execute(String[] args);
}
