package games.adlsv.communicate.eventListener;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface InventoryListener {
    default void listen(InventoryClickEvent e) {

    }
}
