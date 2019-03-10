package net.comorevi.discordconnector.core;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;

import java.io.File;
import java.io.IOException;

public class Main extends PluginBase {

    private String token;
    private String userName;

    @Override
    public void onEnable() {
        new File("./plugins/DiscordConnector/").mkdirs();
        init();

        //DiscordProvider.listen(token);
        //DiscordProvider2.start(token);
        DiscordProvider.start(token, userName);
    }

    private void init() {
        try {
            if (!new File("./plugins/DiscordConnector/Config.yml").exists()) {
                Utils.writeFile(new File("./plugins/DiscordConnector/Config.yml"), this.getClass().getClassLoader().getResourceAsStream("Config.yml"));
            }

            Config config = new Config();
            config.load("./plugins/DiscordConnector/Config.yml");
            token = config.getString("Token");
            userName = config.getString("UserName");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
