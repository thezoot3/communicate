package games.adlsv.communicate.api.util;

import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.mongoDB.PlayerSocialInfoPath;
import games.adlsv.mongoDBAPI.MongoDBDocument;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class DataUtil {
    public static String getDiscordIdFromPlayer(OfflinePlayer p) {
        Document doc = (Document) PlayerSocialCollections.Path.USERS.getCollection().getCollection().find(eq("connectcode", p.getUniqueId().toString())).first();
        if(doc != null) {
            return doc.getString("id");
        } else {
            return null;
        }
    }
    public static String getPlayerUUIDFromDiscordId(String id) {
        Document doc = (Document) PlayerSocialCollections.Path.USERS.getCollection().getCollection().find(eq("id", id)).first();
        if(doc != null) {
            if(doc.getBoolean("certified")) {
                return doc.getString("connectcode");
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public static boolean isPlayerExist(String s) {
        try {
            UUID uuid = getPlayerFromString(s).getUniqueId();
            Document doc = (Document) PlayerSocialCollections.Path.USERS.getCollection().getCollection().find(eq("connectcode", uuid.toString())).first();
            return doc != null;
        } catch(NullPointerException e) {
            return false;
        }
    }
    public static OfflinePlayer getPlayerFromString(String s) throws NullPointerException {
        String ps = ChatColor.stripColor(s);
        return Bukkit.getOfflinePlayer(ps) != null ?
                        Bukkit.getOfflinePlayer(ps) :
                        null;
    }
    public static boolean isVerified(OfflinePlayer p) {
        try {
            MongoDBDocument doc = new MongoDBDocument(PlayerSocialCollections.Path.USERS.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
            return doc.get(PlayerSocialInfoPath.Path.CERTIFIED.getPath().getPath()).getAsBoolean();
        } catch(Exception ignored) {
            return false;
        }
    }
}
