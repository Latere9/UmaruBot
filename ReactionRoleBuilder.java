package umaru;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;

import java.awt.*;

public class ReactionRoleBuilder {

    private TextChannel reactionRoleChannel;
    private Long messageID;
    private EmbedBuilder messageContents;
    private String[] listOfReactions;
    private Role[] listOfRoles;

    public ReactionRoleBuilder(TextChannel tc, String name, Color c,
                               Role[] roles, String[] reactions, Long messageID) {
        messageContents = new EmbedBuilder()
                .setTitle(name)
                .setColor(c);
        this.reactionRoleChannel = tc;
        this.listOfRoles = roles;
        listOfReactions = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            listOfReactions[i] = EmojiParser.parseToUnicode(reactions[i]);
        }
        this.messageID = messageID;
    }

    public ReactionRoleBuilder(TextChannel tc, String name, Color c,
                               Role[] roles, String[] reactions) {
        this(tc, name, c, roles, reactions, -1L);
        this.messageID = initializeRoleReactionMessage();
    }

    public void addAddRoleListener() {
        reactionRoleChannel.getMessageById(messageID).join().addReactionAddListener(a -> {
            String re = a.getEmoji().asUnicodeEmoji().get();
            int index =  getIndexOf(re, listOfReactions);
            if (index >= 0 && index < listOfRoles.length) {
                a.requestUser().join().addRole(listOfRoles[index]);
            }
        });
    }

    public void addRemoveRoleListener() {
        reactionRoleChannel.getMessageById(messageID).join().addReactionRemoveListener(r -> {
            String re = r.getEmoji().asUnicodeEmoji().get();
            int index = getIndexOf(re, listOfReactions);
            if (index >= 0 && index < listOfRoles.length) {
                r.requestUser().join().removeRole(listOfRoles[index]);
            }
        });
    }

    private Long initializeRoleReactionMessage() {
        String description = "";
        for (int i = 0; i < listOfRoles.length; i++)  {
            description = description + "React with " + listOfReactions[i] + " for " + listOfRoles[i].getName() + "!\n";
        }
        Long[] messageID = new Long[1];
        messageContents.setDescription(description);
        reactionRoleChannel.sendMessage(messageContents).thenCompose(message -> {
            message.addReactions(listOfReactions);
            messageID[0] = message.getId();
            return null;
        });
        return messageID[0];
    }

    private static int getIndexOf(String expression, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (expression.equals(array[i])) {
                return i;
            }
        }
        return -1;
    }

    public Long getMessageID() {
        return messageID;
    }
}
