package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.eventListener.InventoryListener.InventoryListener;
import net.kyori.adventure.text.TextComponent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;


public class InventoryClickEvent implements Listener {
    @EventHandler
    public void InventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        String invname = ((TextComponent) e.getView().title()).content();
        InventoryListener listener = getExistHandlerContainsTitle(invname);
        if(listener != null) {
            listener.listen(e);
        }
    }
    private static InventoryListener getExistHandlerContainsTitle(String s) {
        for(String title : listeners.keySet()) {
            if(s.contains(title)) {
                return listeners.get(title);
            }
        }
        return null;
    }
    private static final HashMap<String, InventoryListener> listeners = new HashMap<>();
    public static void setHandler(String invname, InventoryListener object) {
        listeners.put(invname, object);
    }
}
