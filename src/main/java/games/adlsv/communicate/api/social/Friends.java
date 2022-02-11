package games.adlsv.communicate.api.social;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Friends {
    public Player player;
    public Friends(Player p ) {
     player = p;
    }
    public ArrayList<String> getFriendList() {
        PlayerSocialInfo info = new PlayerSocialInfo(player);
        ArrayList<String> list = new ArrayList<>();
        for(JsonElement e: info.get(PlayerSocialInfo.pathList.FRIENDS).getAsJsonArray()) {
            list.add(e.getAsString());
        }
        return list;
    }
    public boolean isFriends(Player p) {
        return this.getFriendList().contains(p.getName());
    }
    public boolean addFriendRequest(Player p) {
        if(this.isFriends(p)) {
            return false;
        } else {
            PlayerSocialInfo info = new PlayerSocialInfo(player);
            info.addValueToArray(PlayerSocialInfo.pathList.FRIENDS_REQUEST, p.getName());
            return true;
        }
    }
    public boolean declineFriendRequest(Player p) {
        if(this.isFriends(p)) {
            PlayerSocialInfo info = new PlayerSocialInfo(player);
            info.removeValueToArray(PlayerSocialInfo.pathList.FRIENDS_REQUEST, p.getName());
            return true;
        } else {
            return false;
        }
    }
}
