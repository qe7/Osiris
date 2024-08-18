package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.relations.Relation;
import io.github.qe7.features.relations.enums.RelationType;
import io.github.qe7.utils.local.ChatUtility;

import java.util.List;
import java.util.stream.Collectors;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("Friend", "Whitelists a player");

        this.setAliases(new String[]{"f", "friends", "whitelist"});
        this.setUsage("friend <add/del/list> <player>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2 || args.length > 3) {
            ChatUtility.addPrefixedMessage("Friend", "Invalid arguments");
            ChatUtility.addPrefixedMessage("Friend", this.getUsage());
            return;
        }

        final String action = args[1];

        switch (action.toLowerCase()) {
            case "add": {
                if (Osiris.getInstance().getRelationManager().isFriend(args[2])) {
                    ChatUtility.addPrefixedMessage("Friend", "Already friends with " + args[2]);
                    return;
                }
                if (Osiris.getInstance().getRelationManager().isEnemy(args[2])) {
                    ChatUtility.addPrefixedMessage("Friend", "Already enemies with " + args[2]);
                    return;
                }
                Osiris.getInstance().getRelationManager().addRelation(args[2], RelationType.FRIEND);
                ChatUtility.addPrefixedMessage("Friend", "Added " + args[2] + " to friends");
                break;
            }
            case "del": {
                if (!Osiris.getInstance().getRelationManager().isFriend(args[2])) {
                    ChatUtility.addPrefixedMessage("Friend", "Not friends with " + args[2]);
                    return;
                }
                Osiris.getInstance().getRelationManager().removeRelation(args[2]);
                ChatUtility.addPrefixedMessage("Friend", "Removed " + args[2] + "from friends");
                break;
            }
            case "list": {
                List<Relation> friends = Osiris.getInstance().getRelationManager().getMap().keySet().stream().filter(relation -> relation.getType() == RelationType.FRIEND).collect(Collectors.toList());

                if (friends.isEmpty()) {
                    ChatUtility.addPrefixedMessage("Friend", "No friends");
                    return;
                }

                String display = friends.stream().map(Relation::getName).reduce((a, b) -> a + ", " + b).orElse("");

                ChatUtility.addPrefixedMessage("Friend", "Friends: " + display);
                break;
            }
            default:
                ChatUtility.addPrefixedMessage("Friend", "Invalid action");
        }
    }
}
