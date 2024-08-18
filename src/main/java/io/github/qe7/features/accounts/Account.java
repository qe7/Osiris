package io.github.qe7.features.accounts;

import com.google.gson.JsonObject;
import io.github.qe7.utils.configs.Serialized;

import java.util.HashMap;

public class Account implements Serialized {

    private final String username;

    private String password;

    public Account(String username) {
        this.username = username;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
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
