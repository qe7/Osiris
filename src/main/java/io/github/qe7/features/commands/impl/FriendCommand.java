package io.github.qe7.features.commands.impl;

import io.github.qe7.Osiris;
import io.github.qe7.features.commands.api.Command;
import io.github.qe7.features.friends.Friend;
import io.github.qe7.features.friends.enums.FriendType;
import io.github.qe7.utils.local.ChatUtility;

public class FriendCommand extends Command {

    public FriendCommand() {
        super("Friend", "Whitelists a player");

        this.setAliases(new String[]{"f"});
        this.setUsage("friend <add/del/list> <player>");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2 || args.length > 3) {
            ChatUtility.sendPrefixedMessage("!", "Invalid arguments");
            ChatUtility.sendPrefixedMessage("!", this.getUsage());
            return;
        }

        final String action = args[1];

        switch (action.toLowerCase()) {
            case "add": {
                if (Osiris.getInstance().getFriendManager().isFriend(args[2])) {
                    ChatUtility.sendPrefixedMessage("!", "Already friends with " + args[2]);
                    return;
                }
                Osiris.getInstance().getFriendManager().addFriend(args[2], FriendType.FRIEND);
                ChatUtility.sendPrefixedMessage("!", "Added " + args[2] + " to friends");
                break;
            }
            case "del": {
                if (!Osiris.getInstance().getFriendManager().isFriend(args[2])) {
                    ChatUtility.sendPrefixedMessage("!", "Not friends with " + args[2]);
                    return;
                }
                Osiris.getInstance().getFriendManager().removeFriend(args[2]);
                ChatUtility.sendPrefixedMessage("!", "Removed " + args[2] + "from friends");
                break;
            }
            case "list": {
                if (Osiris.getInstance().getFriendManager().getMap().isEmpty()) {
                    ChatUtility.sendPrefixedMessage("!", "No friends");
                    return;
                }
                String friends = Osiris.getInstance().getFriendManager().getMap().keySet().stream().map(Friend::getName).reduce((a, b) -> a + ", " + b).orElse("");
                ChatUtility.sendPrefixedMessage("!", "Friends: " + friends);
                break;
            }
            default: ChatUtility.sendPrefixedMessage("!", "Invalid action");
        }
    }
}
