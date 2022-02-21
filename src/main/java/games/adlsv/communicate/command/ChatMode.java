package games.adlsv.communicate.command;

import games.adlsv.communicate.api.chatting.Prefix;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChatMode implements CommandExecutor {
    public static final HashMap<Player, Prefix> chatmode = new HashMap<>();
    private static final ArrayList<Prefix> modes = new ArrayList<>(Arrays.asList(
            Prefix.CHATMODE_서버,
            Prefix.CHATMODE_월드,
            Prefix.CHATMODE_로컬,
            Prefix.CHATMODE_방송
    ));
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Inventory inv = Bukkit.createInventory((Player) sender, 27, Component.text("채팅 모드 설정"));
        ((Player) sender).openInventory(reloadChatModeInv((Player) sender, inv));
        return true;
    }
    public static Inventory reloadChatModeInv(Player p, Inventory inv) {
        for(int i = 0; i < modes.size(); i++) {
            ItemStack item = new ItemStack(Material.RED_STAINED_GLASS);
            ItemMeta itemmeta = item.getItemMeta();
            itemmeta.displayName(Component.text(modes.get(i).value));
            List<Component> lore = Arrays.asList(
                    Component.text(modes.get(i).info),
                    Component.text(ChatColor.translateAlternateColorCodes('&',"&f&l현재 &c&l비활성화&f&l 되어있습니다.")));
            if(chatmode.get(p) == modes.get(i)) {
                item.setType(Material.GREEN_STAINED_GLASS);
                lore.set(1, Component.text(ChatColor.translateAlternateColorCodes('&',"&f&l현재 &a&l활성화&f&l 되어있습니다.")));
            }
            itemmeta.lore(lore);
            item.setItemMeta(itemmeta);
            inv.setItem(i * 2 + 10, item);
        }
        return inv;
    }
}

