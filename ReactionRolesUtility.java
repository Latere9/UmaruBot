package umaru;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.Role;

public class ReactionRolesUtility {
    protected static void roleAddListener(TextChannel tc, Long roleReactMessage,
                                          Role[] roles, String[] reactions) {
        tc.getMessageById(roleReactMessage).join().addReactionAddListener(a -> {
            String re = a.getEmoji().asUnicodeEmoji().get();
            int index =  getIndexOf(re, reactions);
            if (index >= 0 && index < roles.length) {
                a.requestUser().join().addRole(roles[index]);
            }
        });
    }

    protected static void roleRemovedListener(TextChannel tc, Long roleReactMessage,
                                              Role[] roles, String[] reactions) {
        tc.getMessageById(roleReactMessage).join().addReactionRemoveListener(r -> {
            String re = r.getEmoji().asUnicodeEmoji().get();
            int index = getIndexOf(re, reactions);
            if (index >= 0 && index < roles.length) {
                r.requestUser().join().removeRole(roles[index]);
            }
        });
    }

    private static int getIndexOf(String expression, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (expression.equals(EmojiParser.parseToUnicode(array[i]))) {
                return i;
            }
        }
        return -1;
    }
}
