package games.adlsv.communicate.api.social;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.chatting.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Ignores {
    public Player player;
    public Ignores(Player p) {
     player = p;
    }
    private PlayerSocialInfo info;
    public ArrayList<String> getIgnoresList() {
        info = new PlayerSocialInfo(player);
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfo.pathList.IGNORES).getAsJsonArray()) {
            list.add(e.getAsString());
        }
        return list;
    }
    public boolean isIgnores(OfflinePlayer p) {
        return this.getIgnoresList().contains(p.getUniqueId().toString());
    }
    public void addIgnores(OfflinePlayer p) {
        if(!this.isIgnores(p)) {
                info.addValueToArray(PlayerSocialInfo.pathList.IGNORES, p.getUniqueId().toString());
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
            info.removeValueFromArray(PlayerSocialInfo.pathList.IGNORES, p.getUniqueId().toString());
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
