package games.adlsv.communicate.api.util;

import games.adlsv.communicate.api.mongoDB.MongoDBCollections;
import org.bson.Document;
import org.bukkit.entity.Player;

import static com.mongodb.client.model.Filters.eq;

public class dataUtil {
    public static String getDiscordIdFromPlayer(Player p) {
        Document doc = (Document) MongoDBCollections.USERS.getCollection().find(eq("connectcode", p.getName())).first();
        if(doc != null) {
            return doc.getString("id");
        } else {
            return null;
        }
    }
    public static String getPlayernameFromDiscordId(String id) {
        Document doc = (Document) MongoDBCollections.USERS.getCollection().find(eq("id", id)).first();
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
}
