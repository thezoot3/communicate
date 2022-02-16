package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.chatting.Prefix;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import games.adlsv.communicate.command.ChatMode;

public class ChatModeInvListener implements InventoryListener {
    @Override
    public void listen(InventoryClickEvent e) {
        if(e.getCurrentItem() != null) {
            if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS || e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS) {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();
                Prefix mode = ChatMode.chatmode.get(p);
                String itemName = ((TextComponent) e.getCurrentItem().getItemMeta().displayName()).content();
                String modeName = ChatColor.stripColor(itemName.replace("〔","").replace("〕","")).replace(" ", "");
                if(mode != null && mode.value.equals(itemName)) { // 이미 활성화 된거 눌렀을때
                    ChatMode.chatmode.remove(p);
                } else if (mode != null) { // 이미 다른게 선택된 상황에서 바꿀때
                    ChatMode.chatmode.replace(p, Prefix.valueOf("CHATMODE_" + modeName));
                } else { // 아예 아무것도 선택 안되어있었을때
                    ChatMode.chatmode.put(p, Prefix.valueOf("CHATMODE_" + modeName));
                }
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, -5);
                ChatMode.reloadChatModeInv(p, e.getInventory());
            };
        }
    }
}
