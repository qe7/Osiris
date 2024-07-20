package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.features.friends.Friend;
import io.github.qe7.features.friends.enums.FriendType;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.utils.configs.FileUtility;

public final class FriendManager extends Manager<Friend, String> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void initialise() {
        System.out.println("FriendManager initialising!");

        this.loadFriends();

        System.out.println("FriendManager initialised!");
    }

    @Override
    public void register(final Friend friend) {
        this.getMap().put(friend, friend.getName());
    }

    @Override
    public void unregister(final Friend friend) {
        this.getMap().remove(friend);
    }

    public boolean isFriend(final String name) {
        return this.getMap().values().stream().anyMatch(friend -> friend.equalsIgnoreCase(name));
    }

    public void addFriend(final String name, final FriendType type) {
        final Friend friend = new Friend(name, name, type);

        this.register(friend);

        this.saveFriends();
    }

    public void removeFriend(final String name) {
        this.getMap().entrySet().removeIf(entry -> entry.getKey().getName().equalsIgnoreCase(name));

        this.saveFriends();
    }

    public void saveFriends() {
        final JsonObject object = new JsonObject();

        this.getMap().forEach((friend, name) -> object.add(name, friend.serialize()));

        FileUtility.writeFile("friends", GSON.toJson(object));
    }

    public void loadFriends() {
        final String config = FileUtility.readFile("friends");

        if (config == null) {
            return;
        }

        final JsonObject object = GSON.fromJson(config, JsonObject.class);

        object.entrySet().forEach(entry -> {
            final Friend friend = new Friend(entry.getKey(), entry.getKey(), FriendType.valueOf(entry.getValue().getAsJsonObject().get("type").getAsString()));

            this.getMap().put(friend, entry.getKey());
        });
    }
}
