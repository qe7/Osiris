package io.github.qe7.features.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Feature {

    private final String name, description;

    public Feature(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
