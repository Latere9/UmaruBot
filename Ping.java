package umaru;

import org.javacord.api.DiscordApi;

public class Ping {
    protected static void creatPingListener(DiscordApi api) {
        api.addMessageCreateListener(e -> {
            if (e.getMessageContent().equalsIgnoreCase("!ping")) {
                e.getChannel().sendMessage("Pong!");
            }
        });
    }
}
