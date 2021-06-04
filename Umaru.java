package umaru;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

import java.awt.Color;

import java.io.*;
import java.util.Properties;

public class Umaru {

    private static final String[] reactions = {":one:", ":two:",
            ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:"};
    public static void main(String[] args) {
        /*
            Properties Initialization
         */
        Properties props = new Properties();
        File propsfile = new File("umaru.properties");
        try {
            FileReader fr = new FileReader(propsfile);
            props.load(fr);
            fr.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.exit(9);
        }

        //PrintStream ps = new PrintStream(System.out);
        //props.list(ps);
        //Creates an apibuilder object, and joins with token.
        DiscordApi api = new DiscordApiBuilder().setToken(props.getProperty("token"))
                .login().join();
        //Sets some variables etc that we will use later
        Server wdm = (api.getServerById(props.getProperty("server_id")).isEmpty()) ? null
                : api.getServerById(props.getProperty("server_id")).get();

        /*
            RANDOM MESSAGE LISTENERS
         */

        //Respond with Pong to ping.
        Ping.creatPingListener(api);

        /*
            Welcome Message
         */

        TextChannel welcome = (wdm.getTextChannelById(props.getProperty("welcome_channel")).isEmpty()) ? null
                : wdm.getTextChannelById(props.getProperty("welcome_channel")).get();
        WelcomeMessageBuilder welcomeRoleBuilder;
        String welcomeMessage = "Hello! Welcome to the Winged Death Machine discord server!";
        String welcomeDescription = "You may have noticed there isn't anything in the server you have access to. " +
                "Just read the rules (or don't, we honestly don't care) and then react to this message for access. The bot should update your roles." +
                "If it doesn't, please DM an admin. Afterwards, head to the role assign channel to get some game-related roles (or don't, again you do what you want)." +
                "We hope you enjoy your time here!";

//        if (welcome.getMessageById(props.getProperty("welcome_message")).isCancelled() || props.getProperty("welcome_message") == null) {
//            welcomeRoleBuilder = new WelcomeMessageBuilder(welcome, welcomeMessage, Color.ORANGE, welcomeDescription,
//                    wdm.getRoleById(props.getProperty("member_role")).get());
//            updateProps(props, "welcome_message", String.valueOf(welcomeRoleBuilder.getMessageID()));
//        } else {
//            welcomeRoleBuilder = new WelcomeMessageBuilder(welcome, welcomeMessage, Color.ORANGE, welcomeDescription,
//                    wdm.getRoleById(props.getProperty("member_role")).get(), Long.parseLong(props.getProperty("welcome_message")));
//        }
//        welcomeRoleBuilder.addAddRoleListener();
        try {
            welcomeRoleBuilder = new WelcomeMessageBuilder(welcome, welcomeMessage, Color.ORANGE, welcomeDescription,
                    wdm.getRoleById(props.getProperty("member_role")).get(), Long.parseLong(props.getProperty("welcome_message")));
            welcomeRoleBuilder.addAddRoleListener();
        } catch (Exception e) {
            welcomeRoleBuilder = new WelcomeMessageBuilder(welcome, welcomeMessage, Color.ORANGE, welcomeDescription,
                    wdm.getRoleById(props.getProperty("member_role")).get());
            updateProps(props, "welcome_message", String.valueOf(welcomeRoleBuilder.getMessageID()));
            welcomeRoleBuilder.addAddRoleListener();
        }

        /*
            REACTION ROLES
         */
        TextChannel roleAssign = (wdm.getTextChannelById(props.getProperty("role_channel_id")).isEmpty()) ? null
                : wdm.getTextChannelById(props.getProperty("role_channel_id")).get();

        //Game Roles
        int numGameRoles = Integer.parseInt(props.getProperty("count_game_roles"));
        String[] listOfGameRoles = {props.getProperty("amongus_role"),
                props.getProperty("league_role"),
                props.getProperty("valorant_role"),
                props.getProperty("overwatch_role"),
                props.getProperty("minecraft_role"),
                props.getProperty("destiny_role")};
        Role[] gameRoles = new Role[numGameRoles];
        String[] gameRoleReactions = new String[numGameRoles];
        for (int i = 0; i < numGameRoles; i++) {
            if (wdm.getRoleById(listOfGameRoles[i]).isPresent()) {
                gameRoles[i] = wdm.getRoleById(listOfGameRoles[i]).get();
            }
            gameRoleReactions[i] = reactions[i];
        }
        ReactionRoleBuilder gameRoleBuilder;
//        if (!roleAssign.getMessageById(props.getProperty("game_role_message")).isDone() || props.getProperty("game_role_message") == null) {
//            gameRoleBuilder = new ReactionRoleBuilder(roleAssign, "React here for game related roles!",
//                    Color.ORANGE, gameRoles, gameRoleReactions);
//            updateProps(props, "game_role_message", String.valueOf(gameRoleBuilder.getMessageID()));
//        } else {
//            gameRoleBuilder = new ReactionRoleBuilder(roleAssign, "React here for game related roles!",
//                    Color.ORANGE, gameRoles, gameRoleReactions, Long.parseLong(props.getProperty("game_role_message")));
//        }
//        gameRoleBuilder.addAddRoleListener();
//        gameRoleBuilder.addRemoveRoleListener();
        try {
            gameRoleBuilder = new ReactionRoleBuilder(roleAssign, "React here for game related roles!",
                    Color.ORANGE, gameRoles, gameRoleReactions, Long.parseLong(props.getProperty("game_role_message")));
            gameRoleBuilder.addAddRoleListener();
            gameRoleBuilder.addRemoveRoleListener();
        } catch (Exception e) {
            gameRoleBuilder = new ReactionRoleBuilder(roleAssign, "React here for game related roles!",
                    Color.ORANGE, gameRoles, gameRoleReactions);
            updateProps(props, "game_role_message", String.valueOf(gameRoleBuilder.getMessageID()));
            gameRoleBuilder.addAddRoleListener();
            gameRoleBuilder.addRemoveRoleListener();
        }
    }

    private static void updateProps(Properties props, String key, String value) {
        props.setProperty(key, value);
        try {
            File propsfile = new File("umaru.properties");
            FileWriter fw = new FileWriter(propsfile);
            props.store(fw, "umaru properties");
            fw.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}

//Im just gonna put this here lol
//        props.setProperty("token",IDs.BOT_TOKEN);
//        props.setProperty("server_id",Long.toString(IDs.SERVER_ID));
//        props.setProperty("role_channel_id",Long.toString(IDs.ROLE_CHANNEL_ID));
//        props.setProperty("game_role_message",Long.toString(gameRoleMessage));
//        props.setProperty("amongus_role",Long.toString(IDs.AMONG_US));
//        props.setProperty("league_role",Long.toString(IDs.LEAGUE));
//        props.setProperty("valorant_role",Long.toString(IDs.VALORANT));
//        props.setProperty("overwatch_role",Long.toString(IDs.OVERWATCH));
//        props.setProperty("minecraft_role",Long.toString(IDs.MINECRAFT));
//        props.setProperty("destiny_role",Long.toString(IDs.DESTINY));
//        try {
//            FileWriter fw = new FileWriter(propsfile);
//            props.store(fw, "umaru properties");
//            fw.close();
//        } catch (IOException io) {
//            System.out.println(io.getMessage());
//            System.exit(9);
//        }