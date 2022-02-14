package games.adlsv.communicate;

import games.adlsv.communicate.api.mongoDB.MongoDBCollections;
import games.adlsv.communicate.api.mongoDB.MongoDBDocument;
import games.adlsv.communicate.command.*;
import games.adlsv.communicate.eventListener.ChatEventListener;
import games.adlsv.communicate.eventListener.ChatModeInventoryListener;
import games.adlsv.communicate.eventListener.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import games.adlsv.communicate.eventListener.JoinEventListener;



public final class Communicate extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new ChatEventListener(), this);
        this.getCommand("verify").setExecutor(new VerifyCommand());
        this.getCommand("chatmode").setExecutor(new ChatMode());
        this.getCommand("prefix").setExecutor(new PlayerPrefix());
        this.getCommand("profile").setExecutor(new Profile());
        this.getCommand("friend").setExecutor(new Friend());
        InventoryClickEvent.setHandler("채팅 모드 설정", new ChatModeInventoryListener());
    }
    @Override

    public void onDisable() {
        // Plugin shutdown logic
    }
}
