package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.social.Friends;
import games.adlsv.communicate.api.util.DataUtil;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProfileInvListener implements InventoryListener {
    @Override
    public void listen(InventoryClickEvent e) {
        String title = ((TextComponent) e.getView().title()).content().split("님")[0];
        Player p = (Player) e.getWhoClicked();
        OfflinePlayer profilePlayer = DataUtil.getPlayerFromString(title);
        Friends friends = new Friends(p);
        switch (e.getRawSlot()) {
            case 29: // DM 시작
                e.getWhoClicked().closeInventory();
                p.performCommand("/귓속말 " + profilePlayer.getName());
                break;
            case 31: // 친구 요청
                e.getWhoClicked().closeInventory();
                friends.sendFriendRequest(profilePlayer);
                break;
            case 33: // 차단
                e.getWhoClicked().closeInventory();
                System.out.println("1");
                break;
        }
    }
}
