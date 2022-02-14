package games.adlsv.communicate.api.social;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.chatting.Prefix;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Friends {
    public Player player;
    public Friends(Player p) {
     player = p;
    }
    private PlayerSocialInfo info;
    public ArrayList<String> getFriendList() {
        info = new PlayerSocialInfo(player);
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfo.pathList.FRIENDS).getAsJsonArray()) {
            list.add(e.getAsString());
        }
        return list;
    }
    public ArrayList<String> getFriendRequestList() {
        info = new PlayerSocialInfo(player);
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfo.pathList.FRIENDS_REQUEST).getAsJsonArray()) {
            list.add(e.getAsString());
        }
        return list;
    }
    public boolean isFriends(Player p) {
        return this.getFriendList().contains(p.getUniqueId().toString());
    }
    public void declineFriendRequest(Player p) {
        if(isFriends(p) && isFriendRequestExists(p)) {
            info.removeValueToArray(PlayerSocialInfo.pathList.FRIENDS_REQUEST, p.getUniqueId().toString());
            StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value)
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님의 친구 요청을 거절했습니다.");
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
        } else {
            String message = Prefix.CHAT + " &f&l해당 친구 요청이 존재하지 않습니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
    public void acceptFriendRequest(Player p) {
        if(this.isFriends(p) && isFriendRequestExists(p)) {
                info.removeValueToArray(PlayerSocialInfo.pathList.FRIENDS_REQUEST, p.getUniqueId().toString()); // 수락한 사람의 정보수정
                info.addValueToArray(PlayerSocialInfo.pathList.FRIENDS, p.getUniqueId().toString());
                PlayerSocialInfo beExecute  = new PlayerSocialInfo(p);
                beExecute.addValueToArray(PlayerSocialInfo.pathList.FRIENDS, player.getUniqueId().toString());
                StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value) // 수락한 사람에게
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님의 친구 요청을 수락했습니다.\n")
                        .append(Prefix.CHAT.value)
                        .append(" &f&l이제 당신은 ")
                        .append(p.getName())
                        .append("와 친구입니다.");
                StringBuilder textToBeExecute = new StringBuilder().append(Prefix.CHAT.value) // 수락당한 사람에게
                        .append(" &f&l")
                        .append(player.getName())
                        .append("님이 당신의 친구 요청을 수락했습니다.\n")
                        .append(Prefix.CHAT.value)
                        .append(" &f&l이제 당신은 ")
                        .append(player.getName())
                        .append("와 친구입니다.");
                player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
                p.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToBeExecute.toString())));
        } else {
            String message = Prefix.CHAT + " &f&l해당 친구 요청이 존재하지 않습니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
    public void sendFriendRequest(Player p){
        if(this.isFriends(p) && !isFriendRequestExists(p)) {
            PlayerSocialInfo beExecute  = new PlayerSocialInfo(p);
            beExecute.addValueToArray(PlayerSocialInfo.pathList.FRIENDS_REQUEST, player.getUniqueId().toString()); // 전송 당한 사람의 정보 수정
            StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value) // 전송한 사람에게
                    .append(" &f&l")
                    .append(player.getName())
                    .append("님에게 친구 요청을 전송하였습니다.");
            StringBuilder textToBeExecute = new StringBuilder().append(Prefix.CHAT.value) // 전송당한 사람에게
                    .append(" &f&l")
                    .append(p.getName())
                    .append("님이 당신에게 친구 추가 요청을 보냈습니다.");
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
            p.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToBeExecute.toString())));
        } else {
            String message = Prefix.CHAT + " &f&l이미 친구 관계이거나 이미 친구 추가 요청이 전송된 플레이어입니다";
            player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
        }

    }
    public boolean isFriendRequestExists(Player p) {
        boolean isRequested = false;
        for(JsonElement e : info.get(PlayerSocialInfo.pathList.FRIENDS_REQUEST).getAsJsonArray()) {
            if(e.getAsString().equals(p.getUniqueId().toString())) {
                isRequested = true;
                break;
            }
        }
        return isRequested;
    }
}
