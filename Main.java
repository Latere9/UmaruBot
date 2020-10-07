package Umaru;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.*;

public class Main {

    public static final String BOT_TOKEN = "<your token here>"

    public static void main(String[] args) {

        //Creates an apibuilder object, and joins with token.
        DiscordApi api = new DiscordApiBuilder().setToken(BOT_TOKEN)
                .login().join();


        /*
            RANDOM MESSAGE LISTENERS
         */

        //Respond with Pong to ping.
        api.addMessageCreateListener(e -> {
            if (e.getMessageContent().equalsIgnoreCase("!ping")) {
                e.getChannel().sendMessage("Pong!");
            }
        });

        //React to a message saying "I want to die"
        api.addMessageCreateListener(suicidal -> {
            if (suicidal.getMessageContent().equalsIgnoreCase("I want to die")) {
                suicidal.getChannel().sendMessage("Just do it!");
            }
        });


        /*
            ROLE CHANGE LISTENERS
         */

        //Adds among us role
        api.addMessageCreateListener(e -> {
            if (e.getMessageContent().equalsIgnoreCase("!add au")) {
                 e.getMessageAuthor().asUser().ifPresent(a -> {
                     e.getServer().ifPresent(b -> {
                         b.getRoleById(748005731466215425L).ifPresent(c -> {
                             a.addRole(c);
                         });
                     });
                 });
            }
        });
    }
}
