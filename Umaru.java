package Umaru;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
//import org.javacord.api.util.logging.ExceptionLogger;

import java.util.HashMap;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Umaru {

    public static final String BOT_TOKEN = "";
    public static final String ROLE_CHANNEL_ID = "765597334905552946";
    public static final String SERVER_ID = "239231937481277440";
    public static final Long AMONG_US_ROLE = 748005731466215425L;
    /*
    public static final EmbedBuilder helpDisplay = new EmbedBuilder().setTitle("Umaru Commands")
            .setDescription("The following are all commands utilized by Umaru")
            .setColor(Color.ORANGE)
            .addField("!help", "Displays all commands available to use.")
            .addField("!help <command>", "Displays relevant information about a specific command")
            .addField("!ping", "Pong!")
            .addField("!role", "Displays all relevant commands for altering Discord Roles.");
     */

    public static void main(String[] args) {

        //Creates an apibuilder object, and joins with token.
        DiscordApi api = new DiscordApiBuilder().setToken(BOT_TOKEN)
                .login().join();
        //Sets some variables etc that we will use later
        Server wdm = (api.getServerById(SERVER_ID).isEmpty()) ? null
                : api.getServerById(SERVER_ID).get();
        TextChannel roleAssign = (wdm.getTextChannelById(ROLE_CHANNEL_ID).isEmpty()) ? null
                : wdm.getTextChannelById(ROLE_CHANNEL_ID).get();


        /*
            RANDOM MESSAGE LISTENERS
         */

        //Respond with Pong to ping.
        api.addMessageCreateListener(e -> {
            if (e.getMessageContent().equalsIgnoreCase("!ping")) {
                e.getChannel().sendMessage("Pong!");
            } else if (e.getMessageContent().contains("i want to die")) {
                e.getChannel().sendMessage("Just do it!");
            }
        });


        /*
            UTILITY LISTENER
         */

//        roleAssign.addMessageCreateListener(e -> {
//            if (e.getMessageContent().equalsIgnoreCase("!clear")) {
//                try {
//                    MessageSet history = e.getChannel().getMessages(99).get();
//                    e.getChannel().bulkDelete(history);
//                } catch (Exception exception) {
//                    e.getChannel().sendMessage("Execution Failed, this should not happen please report it to Latere");
//                }
//            }
//        });


        /*
            REACTION ROLES
         */

        //Roles for games
        try {
                MessageSet history = roleAssign.getMessages(Integer.MAX_VALUE).get();
                roleAssign.bulkDelete(history);
        } catch (Exception exception) {
                roleAssign.sendMessage("Execution Failed, this should not happen please report it to Latere");
        }
        EmbedBuilder gameRoles = new EmbedBuilder()
                .setTitle("React here to get roles for specific games!")
                .setDescription("React with :one: for Among Us!")
                .setColor(Color.YELLOW);
        roleAssign.sendMessage(gameRoles).thenCompose(message -> {
            message.addReaction("\u0031\uFE0F\u20E3");
            message.addReactionAddListener(e -> {
                if (e.getUser().isPresent() && e.getUser().get().getId() != 763437747317899304L) {
                    if (e.getEmoji().equalsEmoji("\u0031\uFE0F\u20E3")) {
                        e.getChannel().sendMessage(String.valueOf(e.getServer().get().getRoles(e.getUser().get()).stream().anyMatch(role -> role.getId() == AMONG_US_ROLE)));
//                        if (e.getServer().get().getRoles(e.getUser().get()).stream().anyMatch(role -> role.getId() == AMONG_US_ROLE)) {
//                            //e.getServer().get().getRoleById(AMONG_US_ROLE).get().removeUser(e.getUser().get());
//                        } else {
//                            //e.getServer().get().getRoleById(AMONG_US_ROLE).get().addUser(e.getUser().get());
//                        }
                    }
                    e.removeReaction();
                }
            });
            return null;
        });
    }
}
