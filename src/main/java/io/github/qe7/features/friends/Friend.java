package io.github.qe7.features.friends;

import com.google.gson.JsonObject;
import io.github.qe7.features.friends.enums.FriendType;
import io.github.qe7.utils.configs.Serialized;

public class Friend implements Serialized {

    private final String name;

    private String alias;

    private FriendType type;

    public Friend(final String name, final String alias, final FriendType type) {
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

    public FriendType getType() {
        return this.type;
    }

    public void setType(FriendType type) {
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
        this.type = FriendType.valueOf(object.get("type").getAsString());
    }
}
