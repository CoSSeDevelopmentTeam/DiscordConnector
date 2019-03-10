package net.comorevi.discordconnector.core;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class DiscordProvider2 {

    private static String TOKEN = "TOKEN"; //DiscordBotのトークン

    private static IDiscordClient client;

    public static void start(String token) {
        DiscordProvider2 main = new DiscordProvider2();

        System.out.println("Botを起動中...");

        client = new ClientBuilder().withToken(token).build();
        client.getDispatcher().registerListener(main);
        client.login();
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        System.out.println("Botの準備完了！");
    }

    @EventSubscriber
    public void onMessage(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
        IMessage message = event.getMessage();
        IUser user = message.getAuthor();
        if (user.isBot()) return;

        IChannel channel = message.getChannel();
        String mes = message.getContent();

        if (mes.contains("こんにちは")) {
            channel.sendMessage("こんにちは、" + user.getName() + "さん！");
        }
    }
}
