package games.adlsv.communicate.eventListener;

import net.kyori.adventure.text.TextComponent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;


public class InventoryClickEvent implements Listener {
    @EventHandler
    public void InventoryClickEvent(org.bukkit.event.inventory.InventoryClickEvent e) {
        String invname = ((TextComponent) e.getView().title()).content();
        if(listeners.get(invname) != null){
            listeners.get(invname).listen(e);
        }
    }
    private static HashMap<String, InventoryListener> listeners = new HashMap<>();
    public static void setHandler(String invname, InventoryListener object) {
        listeners.put(invname, object);
    }
}
