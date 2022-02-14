package games.adlsv.communicate.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.util.dataUtil;
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

import java.util.Arrays;

public class Profile implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0 && dataUtil.isPlayerExist(args[0])) {
            openProfile((Player) sender, args[0]);
        } else {
            openProfile((Player) sender, sender.getName());
        }
        return true;
    }
    public static void openProfile(Player toShow, String owner) {
        Player p = (Bukkit.getPlayer(owner) != null) ? Bukkit.getPlayer(owner) : (Player) Bukkit.getOfflinePlayer(owner);
        Inventory inv = Bukkit.createInventory(
                toShow, 54,
                Component.text(ChatColor.translateAlternateColorCodes('&', ChatColor.BOLD + owner + "님의 프로필"))
        );
        PlayerSocialInfo info = new PlayerSocialInfo(p);
        for(int i = 0; i < 6; i++) {
            inv.setItem(i * 9, new ItemStack(Material.GREEN_STAINED_GLASS));
            inv.setItem(i * 9 + 8, new ItemStack(Material.GREEN_STAINED_GLASS));
        }
        simpleItem dmIcon = new simpleItem(Material.OAK_SIGN, "&6&l" + p.getName() + "&f&l님과 DM 시작하기", new String[]{
                "&f&l클릭 시 이 유저와 DM을 시작합니다.",
                "&f&lDM은 다른 유저에게 노출되지 않습니다."
        });
        simpleItem friendIcon = new simpleItem(Material.EMERALD, "&6&l" + p.getName() + "&f&l님에게 친구 요청 보내기");
        for(JsonElement e : info.get(PlayerSocialInfo.pathList.FRIENDS).getAsJsonArray()) {
            if(e.getAsString().equals(toShow.getName())) {
                friendIcon.setName("&6&l" + p.getName() + "&f&l님은 당신의 친구입니다!");
            }
        }
        simpleItem ignoreIcon = new simpleItem(Material.BARRIER, "&6&l" + p.getName() + "&f&l님을 &4&l차단&f&l하기");
        for(JsonElement e : info.get(PlayerSocialInfo.pathList.IGNORES).getAsJsonArray()) {
            if(e.getAsString().equals(toShow.getName())) {
                friendIcon.setName("&6&l" + p.getName() + "&f&l님은 이미 &4&l차단&f&l 된 상태입니다.");
            }
        }
        inv.setItem(29, dmIcon.getItemStack());
        inv.setItem(31, friendIcon.getItemStack());
        inv.setItem(33, ignoreIcon.getItemStack());
        inv.setItem(4, getPlayerSkullWithInfo(p).getItemStack());
        toShow.openInventory(inv);
    }
    public static simpleItem getPlayerSkullWithInfo(Player p) {
        PlayerSocialInfo info = new PlayerSocialInfo(p);
        simpleItem playerHead = new simpleItem(Material.PLAYER_HEAD, ChatColor.BOLD + p.getName() + "님의 프로필");
        SkullMeta meta = ((SkullMeta) playerHead.getItemMeta());
        meta.setOwningPlayer(p);
        playerHead.setItemMeta(meta);
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
        return playerHead;
    }
}
