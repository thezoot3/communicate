package games.adlsv.communicate.eventListener;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryListener {
    public default void listen(InventoryClickEvent e) {

    }
}
