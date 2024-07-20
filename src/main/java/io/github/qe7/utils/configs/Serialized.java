package io.github.qe7.utils.configs;

import com.google.gson.JsonObject;

/**
 * Interface for serializing and deserializing objects
 */
public interface Serialized {

    JsonObject serialize();

    void deserialize(JsonObject object);
}
