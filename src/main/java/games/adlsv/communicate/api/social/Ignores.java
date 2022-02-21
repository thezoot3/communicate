package games.adlsv.communicate.api.social;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.mongoDB.PlayerSocialInfoPath;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.mongoDBAPI.MongoDBDocumentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class Ignores {
    public final Player player;
    public Ignores(Player p) {
     player = p;
    }
    private MongoDBDocumentUtil info;
    public ArrayList<String> getIgnoresList() {
        info = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(),eq("id", DataUtil.getDiscordIdFromPlayer(player)));
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfoPath.Path.IGNORES.getPath()).getAsJsonArray()) {
            if(!e.getAsString().equals("")) {
                list.add(e.getAsString());
            }
        }
        return list;
    }
    public boolean isIgnores(OfflinePlayer p) {
        return this.getIgnoresList().contains(p.getUniqueId().toString());
    }
    public void addIgnores(OfflinePlayer p) {
        if(!this.isIgnores(p)) {
                info.addValueToArray(PlayerSocialInfoPath.Path.IGNORES.getPath(), p.getUniqueId().toString());
                StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value) // 수락한 사람에게
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님을 차단했습니다.\n")
                        .append(Prefix.CHAT.value)
                        .append(" &f&l이제 당신은 ")
                        .append(p.getName())
                        .append("님의 모든 채팅을 볼 수 없습니다.");
                player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
        } else {
            String message = Prefix.CHAT.value + " &f&l이미 차단한 플레이어입니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
    public void removeIgnores(OfflinePlayer p) {
        if(this.isIgnores(p)) {
            info.removeValueFromArray(PlayerSocialInfoPath.Path.IGNORES.getPath(), p.getUniqueId().toString());
            StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value) // 수락한 사람에게
                    .append(" &f&l")
                    .append(p.getName())
                    .append("님을 차단 해제했습니다.\n")
                    .append(Prefix.CHAT.value)
                    .append(" &f&l이제 당신은 ")
                    .append(p.getName())
                    .append("님의 모든 채팅을 볼 수 있습니다.");
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));

        } else {
            String message = Prefix.CHAT.value + " &f&l차단 하지 않은 플레이어입니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
}
