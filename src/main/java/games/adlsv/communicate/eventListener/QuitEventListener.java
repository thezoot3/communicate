package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.mongoDB.PlayerSocialInfoPath;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.mongoDBAPI.MongoDBDocumentUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class QuitEventListener implements Listener {
    @EventHandler
    public void PlayerQuitEventHandler(PlayerQuitEvent e) {
        if(DataUtil.isVerified(e.getPlayer())) {
            MongoDBDocumentUtil info = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(e.getPlayer())));
            info.setValue(PlayerSocialInfoPath.Path.LASTJOIN.getPath(), new Date().toString());
        } else {
            System.out.println(1);
        }
    }
}
