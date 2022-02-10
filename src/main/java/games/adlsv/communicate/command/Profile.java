package games.adlsv.communicate.command;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.util.simpleItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

public class Profile implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0] != null && Bukkit.getPlayer(args[0]) != null) {
            Player p = Bukkit.getPlayer(args[0]);
            Inventory inv = Bukkit.createInventory(
                    (InventoryHolder) sender, 54,
                    Component.text(ChatColor.translateAlternateColorCodes('&', ChatColor.BOLD + args[0] + "님의 창고"))
            );
            PlayerSocialInfo info = new PlayerSocialInfo(Bukkit.getPlayer(args[0]));
            for(int i = 0; i < 6; i++) {
                inv.setItem(i * 9, new ItemStack(Material.GREEN_STAINED_GLASS));
                inv.setItem(i * 9 + 8, new ItemStack(Material.GREEN_STAINED_GLASS));
            }
            simpleItem playerHead = new simpleItem(Material.PLAYER_HEAD, ChatColor.BOLD + p.getName() + "님의 프로필");
            SkullMeta meta = ((SkullMeta) playerHead.getItemMeta());
            meta.setOwningPlayer(p);
            playerHead.applyItemMeta(meta);
            String[] lore = new String[]{
                    new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(
                            (info.get(PlayerSocialInfo.pathList.LASTJOIN).getAsString().equals("online")) ?
                                    "현재 접속 중인 플레이어입니다" :
                                    "마지막 접속 시각: " + info.get(PlayerSocialInfo.pathList.LASTJOIN).getAsString()
                    ).toString(),
                    new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(
                            info.get(PlayerSocialInfo.pathList.INTRODUCE).getAsString() != null ?
                                    "소개: " + info.get(PlayerSocialInfo.pathList.INTRODUCE).getAsString() :
                                    "소개가 없습니다"
                    ).toString()
            };;
            playerHead.setSimpleLore(lore);
            inv.setItem(4, playerHead.getItemStack());
            ((Player) sender).openInventory(inv);
        }
        return false;
    }
}
