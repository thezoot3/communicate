package games.adlsv.communicate.api.chatting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.mongodb.client.model.Updates;
import games.adlsv.communicate.api.mongoDB.MongoDBCollections;
import games.adlsv.communicate.api.mongoDB.MongoDBDocument;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class PlayerSocialInfo {
    private String disId;
    public enum pathList {
        PREFIXES("prefix.prefixes"),
        EQUIPPED("prefix.equipped"),
        IGNORES("ignore"),
        FRIENDS("friend.friends"),
        FRIENDS_REQUEST("friend.requests"),
        LASTJOIN("lastJoin"),
        INTRODUCE("info.introduce");
        pathList(String p) {
            this.path = p;
        }
        public String getPath() {
            return path;
        }
        private final String path;
    }
    public PlayerSocialInfo(Player p) {
        MongoDBDocument doc = new MongoDBDocument(MongoDBCollections.USERS, eq("connectcode", p.getName()));
        if(doc.element != null) {
            this.disId = doc.get("id").getAsString();
        } else {
            TextComponent message = Component.text(Prefix.CHAT.value + " &f&l인증을 완료하지 않으셨습니다.\n" + Prefix.CHAT.value + " &f'&c&lhttps://adlsv.games/login&f'&f&l으로 이동해 인증을 완료하세요");
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.content()));
        }
    }
    public JsonElement get(pathList path) {
        MongoDBDocument doc = new MongoDBDocument(MongoDBCollections.COMMUNICATE, eq("id", disId));
        return doc.get(path.getPath());
    }
    public void addValueToArray(pathList path, String value) {
        try {
            this.get(path).getAsJsonArray();
            Bson update = Updates.push(path.getPath(), value);
            MongoDBCollections.COMMUNICATE.getCollection().updateOne(eq("id", disId), update);
        } catch(IllegalStateException e) {
            throw e;
        }
    }
    public void removeValueToArray(pathList path, String value) {
        try {
            ArrayList<String> list = new ArrayList<>();
            for(JsonElement e: this.get(path).getAsJsonArray()) {
                list.add(e.getAsString());
            }
            if(list.contains(value)) {
                Bson update = Updates.set(path.getPath(), list);
                MongoDBCollections.COMMUNICATE.getCollection().updateOne(eq("id", disId), update);
            }
        } catch(IllegalStateException e) {
            throw e;
        }
    }
}
