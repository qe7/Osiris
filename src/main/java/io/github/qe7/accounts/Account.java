package io.github.qe7.accounts;

import com.google.gson.JsonObject;
import io.github.qe7.utils.configs.Serialized;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Account implements Serialized {

    private final String username;

    @Setter
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();

        object.addProperty("password", this.password);

        return object;
    }

    @Override
    public void deserialize(JsonObject object) {
        this.password = object.get("password").getAsString();
    }
}
