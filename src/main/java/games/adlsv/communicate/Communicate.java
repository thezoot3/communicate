package games.adlsv.communicate;

import games.adlsv.communicate.api.mongoDB.MongoDBCollections;
import games.adlsv.communicate.api.mongoDB.MongoDBDocument;
import games.adlsv.communicate.command.*;
import games.adlsv.communicate.eventListener.*;
import org.bukkit.plugin.java.JavaPlugin;


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
        this.getCommand("ignore").setExecutor(new Ignore());
        this.getCommand("dm").setExecutor(new DirectMessage());
        InventoryClickEvent.setHandler("채팅 모드 설정", new ChatModeInvListener());
        InventoryClickEvent.setHandler("님의 프로필", new ProfileInvListener());
        InventoryClickEvent.setHandler("친구 목록", new FriendInvListener());
        InventoryClickEvent.setHandler("친구 요청 목록", new FriendRequestInvListener());
        InventoryClickEvent.setHandler("차단 목록", new IgnoreInvListener());
    }
    @Override

    public void onDisable() {
        // Plugin shutdown logic
    }
}
