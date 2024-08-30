package io.github.qe7.relations;

import com.google.gson.JsonObject;
import io.github.qe7.relations.enums.RelationType;
import io.github.qe7.utils.configs.Serialized;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Relation implements Serialized {

    private final String name;

    private String alias;

    @Setter
    private RelationType type;

    public Relation(final String name, final String alias, final RelationType type) {
        this.name = name;
        this.alias = alias;
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
