package games.adlsv.communicate.command;

import com.google.gson.JsonArray;
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
            simpleItem dmIcon = new simpleItem(Material.OAK_SIGN, "&6&l" + p.getName() + "님과 DM 시작하기", new String[]{
                    "&f&l클릭 시 이 유저와 DM을 시작합니다.",
                    "&f&lDM은 다른 유저에게 노출되지 않습니다."
            });
            simpleItem friendIcon = new simpleItem(Material.EMERALD, "&6&l" + p.getName() + "님에게 친구 요청 보내기", new String[]{
                    "&f&l클릭 시 이 유저와 DM을 시작합니다.",
                    "&f&lDM은 다른 유저에게 노출되지 않습니다."
            });
            JsonArray array = info.get(PlayerSocialInfo.pathList.FRIENDS).getAsJsonArray();
            for(JsonElement e :array) {
                if(e.getAsString().equals(sender.getName())) {}
            }
            inv.setItem(4, getPlayerSkullWithInfo(p));
            ((Player) sender).openInventory(inv);
        }
        return false;
    }
    public static ItemStack getPlayerSkullWithInfo(Player p) {
        PlayerSocialInfo info = new PlayerSocialInfo(p);
        simpleItem playerHead = new simpleItem(Material.PLAYER_HEAD, ChatColor.BOLD + p.getName() + "님의 프로필");
        SkullMeta meta = ((SkullMeta) playerHead.getItemMeta());
        meta.setOwningPlayer(p);
        playerHead.applyItemMeta(meta);
        String[] lore = new String[]{
                new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(
                        p.isOnline() ?
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
        return playerHead.getItemStack();
    }
}
