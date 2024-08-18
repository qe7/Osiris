package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.relations.Relation;
import io.github.qe7.features.relations.enums.RelationType;
import io.github.qe7.utils.local.ChatUtility;

import java.util.List;
import java.util.stream.Collectors;

public class EnemyCommand extends Command {

    public EnemyCommand() {
        super("Enemy", "Whitelists a player");

        this.setAliases(new String[]{"e", "enemies", "target"});
        this.setUsage("enemy <add/del/list> <player>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2 || args.length > 3) {
            ChatUtility.addPrefixedMessage("Enemy", "Invalid arguments");
            ChatUtility.addPrefixedMessage("Enemy", this.getUsage());
            return;
        }

        final String action = args[1];

        switch (action.toLowerCase()) {
            case "add": {
                if (Osiris.getInstance().getRelationManager().isFriend(args[2])) {
                    ChatUtility.addPrefixedMessage("Enemy", "Already friends with " + args[2]);
                    return;
                }
                if (Osiris.getInstance().getRelationManager().isEnemy(args[2])) {
                    ChatUtility.addPrefixedMessage("Enemy", "Already enemies with " + args[2]);
                    return;
                }
                Osiris.getInstance().getRelationManager().addRelation(args[2], RelationType.ENEMY);
                ChatUtility.addPrefixedMessage("Enemy", "Added " + args[2] + " to enemies");
                break;
            }
            case "del": {
                if (!Osiris.getInstance().getRelationManager().isEnemy(args[2])) {
                    ChatUtility.addPrefixedMessage("Enemy", "Not enemies with " + args[2]);
                    return;
                }
                Osiris.getInstance().getRelationManager().removeRelation(args[2]);
                ChatUtility.addPrefixedMessage("Enemy", "Removed " + args[2] + "from enemies");
                break;
            }
            case "list": {
                List<Relation> enemies = Osiris.getInstance().getRelationManager().getMap().keySet().stream().filter(relation -> relation.getType() == RelationType.ENEMY).collect(Collectors.toList());

                if (enemies.isEmpty()) {
                    ChatUtility.addPrefixedMessage("Enemy", "No enemies");
                    return;
                }

                String display = enemies.stream().map(Relation::getName).reduce((a, b) -> a + ", " + b).orElse("");

                ChatUtility.addPrefixedMessage("Enemy", "Enemies: " + display);
                break;
            }
            default:
                ChatUtility.addPrefixedMessage("Enemy", "Invalid action");
        }
    }
}
