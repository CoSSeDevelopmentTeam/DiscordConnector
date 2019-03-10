package net.comorevi.discordconnector.core;

import cn.nukkit.Server;
import cn.nukkit.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import sun.awt.CharsetString;

import javax.security.auth.login.LoginException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DiscordProvider extends ListenerAdapter {

    private static String userName;

    public static void start(String token, String userName) {
        DiscordProvider.userName = userName;
        try {
            JDA jda = new JDABuilder(token).build();
            jda.addEventListener(new DiscordProvider());
        } catch (LoginException e) {
            Server.getInstance().getLogger().alert("ログインに失敗しました。");
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();

        if (message.startsWith("!")) {
            if (event.isFromType(ChannelType.PRIVATE)) {
                if (!event.getAuthor().getName().endsWith(userName)) {
                    event.getChannel().sendMessage("指定されたユーザーのみが操作できます。");
                    return;
                }

            } else {
                if (!event.getMember().getEffectiveName().endsWith(userName)) {
                    event.getChannel().sendMessage("指定されたユーザーのみが操作できます。");
                    return;
                }
            }

            if (message.substring(1).equals("getlog")) {
                Server.getInstance().getScheduler().scheduleTask(() -> {
                    try {
                        event.getChannel()
                                .sendMessage("最新のサーバーログ")
                                .addFile(new ByteArrayInputStream(Utils.readFile(new File("server.log")).getBytes(Charset.forName("SHIFT-JIS"))), "server.log").queue();
                    } catch (IOException e) {
                        event.getChannel().sendMessage("エラーが発生しました: IOException").queue();
                    }
                });
                return;
            }

            Server.getInstance().getScheduler().scheduleTask(() -> {
                Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), message.substring(1));
                Server.getInstance().getLogger().info("Discordから実行されました: " + message.substring(1));
                event.getChannel().sendMessage("コマンドを実行しました！").queue();
            });

        }
    }

}
