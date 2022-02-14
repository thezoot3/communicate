package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.social.Friends;
import games.adlsv.communicate.api.util.dataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProfileInventoryListener implements InventoryListener {
    @Override
    public void listen(InventoryClickEvent e) {
        String title = e.getView().title().toString().split("님")[0];
        Player p = (Player) e.getWhoClicked();
        Player profilePlayer = dataUtil.getPlayerFromString(title);
        Friends friends = new Friends(p);
        switch (e.getRawSlot()) {
            case 29: // DM 시작

            case 31: // 친구 요청
                e.getView().close();
                friends.sendFriendRequest(profilePlayer);
            case 33: // 차단

        }
    }
}
