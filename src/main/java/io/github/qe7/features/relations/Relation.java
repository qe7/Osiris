package io.github.qe7.features.relations;

import com.google.gson.JsonObject;
import io.github.qe7.features.relations.enums.RelationType;
import io.github.qe7.utils.configs.Serialized;

public class Relation implements Serialized {

    private final String name;

    private String alias;

    private RelationType type;

    public Relation(final String name, final String alias, final RelationType type) {
        this.name = name;
        this.alias = alias;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public RelationType getType() {
        return this.type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("alias", this.alias);
        object.addProperty("type", this.type.name());

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.alias = object.get("alias").getAsString();
        this.type = RelationType.valueOf(object.get("type").getAsString());
    }
}
