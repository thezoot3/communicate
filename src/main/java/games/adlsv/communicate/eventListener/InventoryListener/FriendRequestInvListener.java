package games.adlsv.communicate.eventListener.InventoryListener;

import games.adlsv.communicate.api.social.Friends;
import games.adlsv.communicate.command.Profile;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class FriendRequestInvListener implements InventoryListener {
    @Override
    public void listen(InventoryClickEvent e) {
        if(e.getCurrentItem() != null) {
            OfflinePlayer requestedP = ((SkullMeta) e.getCurrentItem().getItemMeta()).getOwningPlayer();
            Player p = (Player) e.getWhoClicked();
            Friends fri = new Friends(p);
            switch(e.getClick()) {
                case LEFT:
                    p.closeInventory();
                    Profile.openProfile(p, requestedP.getName());
                    break;
                case SHIFT_LEFT:
                    p.closeInventory();
                    fri.acceptFriendRequest(requestedP);
                    break;
                case SHIFT_RIGHT:
                    p.closeInventory();
                    fri.declineFriendRequest(requestedP);
                    break;
            }
        }

    }
}
