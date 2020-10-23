package umaru;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;

import java.awt.*;

public class Initalize {
    protected static Long initializeRoleReactionMessage(TextChannel tc, String name,
                                                        Color c, Role[] roles, String[] reactions) {
        if (roles.length != reactions.length) {
            throw new IllegalArgumentException("The role and reaction string lengths do not match");
        }
        EmbedBuilder roleBuilder = new EmbedBuilder()
                .setTitle(name)
                .setColor(c);
        String description = "";
        for (int i = 0; i < roles.length; i++)  {
            description = description + "React with " + reactions[i] + " for " + roles[i].getName() + "!\n";
            reactions[i] = EmojiParser.parseToUnicode(reactions[i]);
        }
        Long[] messageID = new Long[1];
        roleBuilder.setDescription(description);
        tc.sendMessage(roleBuilder).thenCompose(message -> {
                message.addReactions(reactions);
                messageID[0] = message.getId();
                return null;
        });
        return messageID[0];
    }
}
