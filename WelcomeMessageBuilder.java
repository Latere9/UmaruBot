package umaru;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WelcomeMessageBuilder {

    private TextChannel welcomeChannel;
    private long messageID;
    private EmbedBuilder messageContents;
    private String reaction;
    private Role memberRole;
    private String description;

    public WelcomeMessageBuilder(TextChannel tc, String name, Color c,
                                 String description, Role role, Long messageID) {
        messageContents = new EmbedBuilder()
                .setTitle(name)
                .setColor(c);
        this.welcomeChannel = tc;
        this.memberRole = role;
        this.messageID = messageID;
        this.description = description;
    }

    public WelcomeMessageBuilder(TextChannel tc, String name, Color c,
                                 String description, Role role) {
        this(tc, name, c, description, role, -1L);
        initializeWelcomeMessage();
    }

    private void initializeWelcomeMessage() {
        messageContents.setDescription(description);
        CompletableFuture<Message> welcomeMessage = welcomeChannel.sendMessage(messageContents);
        //try {
        messageID = welcomeMessage.join().getId();
//        } catch (InterruptedException ie) {
//            System.out.println(ie.getMessage());
//            System.out.println(ie.getStackTrace());
//            System.exit(9);
//        } catch (ExecutionException ee) {
//            System.out.println(ee.getMessage());
//            System.out.println(ee.getStackTrace());
//            System.exit(8);
//        }
    }

    public void addAddRoleListener() {
        welcomeChannel.getMessageById(messageID).join().addReactionAddListener(a -> {
            a.requestUser().join().addRole(memberRole);
        });
    }

    public Long getMessageID() {
        return messageID;
    }
}
