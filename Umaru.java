package umaru;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

import java.awt.Color;

public class Umaru {

    private static Long gameRoleMessage = 768976461234307082L;

    public static void main(String[] args) {

        //Creates an apibuilder object, and joins with token.
        DiscordApi api = new DiscordApiBuilder().setToken(IDs.BOT_TOKEN)
                .login().join();
        //Sets some variables etc that we will use later
        Server wdm = (api.getServerById(IDs.SERVER_ID).isEmpty()) ? null
                : api.getServerById(IDs.SERVER_ID).get();


        /*
            RANDOM MESSAGE LISTENERS
         */

        //Respond with Pong to ping.
        Ping.creatPingListener(api);

        /*
            REACTION ROLES
         */
        TextChannel roleAssign = (wdm.getTextChannelById(IDs.ROLE_CHANNEL_ID).isEmpty()) ? null
                : wdm.getTextChannelById(IDs.ROLE_CHANNEL_ID).get();

        //Game Roles
        Role[] gameRoles = new Role[IDs.GAME_ROLES.length];
        String[] gameRoleReactions = new String[IDs.GAME_ROLES.length];
        for (int i = 0; i < IDs.GAME_ROLES.length; i++) {
            if (wdm.getRoleById(IDs.GAME_ROLES[i]).isPresent()) {
                gameRoles[i] = wdm.getRoleById(IDs.GAME_ROLES[i]).get();
            }
            gameRoleReactions[i] = IDs.reactions[i];
        };
        if (roleAssign.getMessageById(gameRoleMessage).isCancelled()) {
            gameRoleMessage = Initalize.initializeRoleReactionMessage(roleAssign,
                    "React here for game related roles!", Color.ORANGE, gameRoles, gameRoleReactions);
        }
        ReactionRolesUtility.roleAddListener(roleAssign, gameRoleMessage, gameRoles, gameRoleReactions);
        ReactionRolesUtility.roleRemovedListener(roleAssign, gameRoleMessage, gameRoles, gameRoleReactions);
    }
}
