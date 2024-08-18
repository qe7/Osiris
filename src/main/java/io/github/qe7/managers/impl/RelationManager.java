package io.github.qe7.managers.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.qe7.features.relations.Relation;
import io.github.qe7.features.relations.enums.RelationType;
import io.github.qe7.managers.api.Manager;
import io.github.qe7.managers.api.interfaces.Register;
import io.github.qe7.managers.api.interfaces.Unregister;
import io.github.qe7.utils.configs.FileUtility;

public final class RelationManager extends Manager<Relation, String> implements Register<Relation>, Unregister<Relation> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void initialise() {
        this.loadRelations();

        System.out.println("RelationManager initialised!");
    }

    @Override
    public void register(final Relation relation) {
        this.getMap().put(relation, relation.getName());
    }

    @Override
    public void unregister(final Relation relation) {
        this.getMap().remove(relation);
    }

    public boolean isFriend(final String name) {
        for (final Relation relation : this.getMap().keySet()) {
            if (relation.getName().equalsIgnoreCase(name) && relation.getType() == RelationType.FRIEND) {
                return true;
            }
        }
        return false;
    }

    public boolean isEnemy(final String name) {
        for (final Relation relation : this.getMap().keySet()) {
            if (relation.getName().equalsIgnoreCase(name) && relation.getType() == RelationType.ENEMY) {
                return true;
            }
        }
        return false;
    }

    public void addRelation(final String name, final RelationType type) {
        final Relation relation = new Relation(name, name, type);

        this.register(relation);

        this.saveRelations();
    }

    public void removeRelation(final String name) {
        this.getMap().entrySet().removeIf(entry -> entry.getKey().getName().equalsIgnoreCase(name));

        this.saveRelations();
    }

    public void saveRelations() {
        final JsonObject object = new JsonObject();

        this.getMap().forEach((relation, name) -> object.add(name, relation.serialize()));

        FileUtility.writeFile("relations", GSON.toJson(object));
    }

    public void loadRelations() {
        final String config = FileUtility.readFile("relations");

        if (config == null) {
            return;
        }

        final JsonObject object = GSON.fromJson(config, JsonObject.class);

        object.entrySet().forEach(entry -> {
            final Relation relation = new Relation(entry.getKey(), entry.getKey(), RelationType.valueOf(entry.getValue().getAsJsonObject().get("type").getAsString()));

            this.getMap().put(relation, entry.getKey());
        });
    }
}
