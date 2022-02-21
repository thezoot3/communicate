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

public class Friends {
    public final Player player;
    public Friends(Player p) {
        player = p;
        info = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(player)));
    }
    private final MongoDBDocumentUtil info;
    public ArrayList<String> getFriendList() {
        try {
            ArrayList<String> list = new ArrayList<>();
            for(JsonElement e: info.get(PlayerSocialInfoPath.Path.FRIENDS.getPath()).getAsJsonArray()) {
                if(!e.getAsString().equals("")) {
                    list.add(e.getAsString());
                }
            }
            return list;
        } catch (RuntimeException e) {
            String message = Prefix.CHAT.value + " &f&l인증이 완료되지 않은 유저입니다.";
            player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
            return null;
        }
    }
    public ArrayList<String> getFriendRequestList() {
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath()).getAsJsonArray()) {
            if(!e.getAsString().equals("")) {
                list.add(e.getAsString());
            }
        }
        return list;
    }
    public boolean isFriends(OfflinePlayer p) {
        return this.getFriendList().contains(p.getUniqueId().toString());
    }
    public void declineFriendRequest(OfflinePlayer p) {
        if(!isFriends(p) && isFriendRequestExists(p)) {
            info.removeValueFromArray(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath(), p.getUniqueId().toString());
            StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value)
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님의 친구 요청을 거절했습니다.");
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
        } else {
            String message = Prefix.CHAT.value + " &f&l해당 친구 요청이 존재하지 않습니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
    public void acceptFriendRequest(OfflinePlayer p) {
        if(!this.isFriends(p) && isFriendRequestExists(p)) {
            try {
                String message = Prefix.CHAT.value + " &f&l인증이 완료되지 않은 유저입니다.";
                player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
                info.removeValueFromArray(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath(), p.getUniqueId().toString()); // 수락한 사람의 정보수정
                info.addValueToArray(PlayerSocialInfoPath.Path.FRIENDS.getPath(), p.getUniqueId().toString());
                MongoDBDocumentUtil beExecute  = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
                beExecute.addValueToArray(PlayerSocialInfoPath.Path.FRIENDS.getPath(), player.getUniqueId().toString());
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
                if(p.getPlayer() != null) {
                    p.getPlayer().sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToBeExecute.toString())));
                }
            } catch (RuntimeException e) {
                String message = Prefix.CHAT.value + " &f&l인증이 완료되지 않은 유저입니다.";
                player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
            }
        } else {
            String message = Prefix.CHAT.value + " &f&l해당 친구 요청이 존재하지 않습니다.";
            player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message)));
        }
    }
    public void sendFriendRequest(OfflinePlayer p){
        if(!this.isFriends(p) && !isFriendRequestExists(p)) {
            try {
                MongoDBDocumentUtil beExecute = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
                beExecute.addValueToArray(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath(), player.getUniqueId().toString()); // 전송 당한 사람의 정보 수정
                StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value) // 전송한 사람에게
                        .append(" &f&l")
                        .append(player.getName())
                        .append("님에게 친구 요청을 전송하였습니다.");
                StringBuilder textToBeExecute = new StringBuilder().append(Prefix.CHAT.value) // 전송당한 사람에게
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님이 당신에게 친구 추가 요청을 보냈습니다.");
                player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
                if(p.getPlayer() != null) {
                    p.getPlayer().sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToBeExecute.toString())));
                }
            } catch (RuntimeException e) {
                String message = Prefix.CHAT.value + " &f&l인증이 완료되지 않은 유저입니다.";
                player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
            }
        } else {
            String message = Prefix.CHAT.value + " &f&l이미 친구 관계이거나 이미 친구 추가 요청이 전송된 플레이어입니다";
            player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
        }

    }
    public void removeFriend(OfflinePlayer p) {
        if(this.isFriends(p)) {
            try {
                info.removeValueFromArray(PlayerSocialInfoPath.Path.FRIENDS.getPath(), p.getUniqueId().toString());
                MongoDBDocumentUtil beExecute  = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
                beExecute.removeValueFromArray(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath(), player.getUniqueId().toString());
                StringBuilder textToExecutor = new StringBuilder().append(Prefix.CHAT.value)
                        .append(" &f&l")
                        .append(player.getName())
                        .append("님은 더 이상 당신의 친구가 아닙니다.");
                StringBuilder textToBeExecute = new StringBuilder().append(Prefix.CHAT.value)
                        .append(" &f&l")
                        .append(p.getName())
                        .append("님이 당신을 친구에서 삭제하였습니다.");
                player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToExecutor.toString())));
                if(p.getPlayer() != null) {
                    p.getPlayer().sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', textToBeExecute.toString())));
                }
            } catch (RuntimeException e) {
                String message = Prefix.CHAT.value + " &f&l인증이 완료되지 않은 유저입니다.";
                player.sendMessage(Component.text(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message)));
            }
        }
    }
    public boolean isFriendRequestExists(OfflinePlayer p) {
        boolean isRequested = false;
        for(JsonElement e : info.get(PlayerSocialInfoPath.Path.FRIENDS_REQUEST.getPath()).getAsJsonArray()) {
            if(e.getAsString().equals(p.getUniqueId().toString())) {
                isRequested = true;
                break;
            }
        }
        return isRequested;
    }
}
