package games.adlsv.communicate.eventListener;
import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.mongoDB.PlayerSocialInfoPath;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.mongoDBAPI.MongoDBDocument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import games.adlsv.communicate.api.chatting.WipeChatting;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class JoinEventListener implements Listener {
    @EventHandler (priority = EventPriority.HIGH)
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        MongoDBDocument doc = new MongoDBDocument(PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(e.getPlayer())));
        long first = Date.parse(doc.get(PlayerSocialInfoPath.Path.FIRSTJOIN.getPath().getPath()).getAsString());
        long last = Date.parse(doc.get(PlayerSocialInfoPath.Path.LASTJOIN.getPath().getPath()).getAsString());
        WipeChatting.wipe(e.getPlayer());
        StringBuilder welcome = new StringBuilder();
        welcome.append(Prefix.CHAT.value)
                .append(" &f&l반갑습니다! &a&l")
                .append(e.getPlayer().getName())
                .append("&f&l님\n");
        if(doc == null) {
            welcome.append(Prefix.CHAT.value)
                    .append(" &f&l아직 &a&l인증&f&l을 하지 않으셨습니다.\n")
                    .append(Prefix.CHAT.value)
                    .append(" &f'&c&lhttps://adlsv.games/login&f'&f&l 으로 접속해 인증 절차를 밟으십시오");
        } else {
            welcome.append(Prefix.CHAT.value)
                    .append(" &f&l현재까지 총 &9&l")
                    .append((first - last)/ 3600)
                    .append("&f&l시간 플레이 하셨습니다.");
        }
        e.joinMessage(null);
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', welcome.toString()));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&f&l[ &a&lJoin&f&l ]&f&l:&f " + e.getPlayer().getName()));
    }

}
