package games.adlsv.communicate.command;

import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.communicate.api.social.Friends;
import games.adlsv.communicate.api.util.SimpleItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class Friend implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0) {
            switch (args[0]) {
                case "요청":
                    if(args.length != 1&&DataUtil.isPlayerExist(args[1])) {
                        Friends f = new Friends((Player) sender);
                        f.sendFriendRequest(DataUtil.getPlayerFromString(args[1]));
                    }
                    break;
                case "거절":
                    if(args.length != 1&&DataUtil.isPlayerExist(args[1])) {
                        Friends f = new Friends((Player) sender);
                        f.declineFriendRequest(DataUtil.getPlayerFromString(args[1]));
                    }
                    break;
                case "수락":
                    if(args.length != 1&&DataUtil.isPlayerExist(args[1])) {
                        Friends f = new Friends((Player) sender);
                        f.acceptFriendRequest(DataUtil.getPlayerFromString(args[1]));
                    }
                    break;
                case "목록":
                    Inventory inv = Bukkit.createInventory((InventoryHolder) sender, 54, Component.text("친구 목록"));
                    Friends fInfo = new Friends((Player) sender);
                    ArrayList<String> fList = fInfo.getFriendList();
                    for(int i = 0; i < fList.size(); i++) {
                        Player friend = (Player) Bukkit.getOfflinePlayer(UUID.fromString(fList.get(i)));
                        SimpleItem item = Profile.getPlayerSkullWithInfo(friend);
                        String[] lores = new String[] {
                                "&f&l당신의 친구인 " + friend.getName() + "님입니다.",
                                "&f&l좌클릭하여 친구의 프로필을 봅니다.",
                                "&f&l우클릭하여 해당 친구을 제거합니다.",
                                "&f&l────[ &b&l정보 &f&l]────"
                        };
                        lores = (String[]) ArrayUtils.addAll(lores, item.getOriginalLores());
                        item.setSimpleLore(lores);
                        inv.setItem(i, item.getItemStack());
                    }
                    ((Player) sender).openInventory(inv);
                    break;
                case "요청목록":
                    Inventory inv2 = Bukkit.createInventory((InventoryHolder) sender, 54, Component.text("친구 요청 목록"));
                    Friends fInfo2 = new Friends((Player) sender);
                    ArrayList<String> fList2 = fInfo2.getFriendRequestList();
                    for(int i = 0; i < fList2.size(); i++) {
                        Player friend = (Player) Bukkit.getOfflinePlayer(UUID.fromString(fList2.get(i)));
                        SimpleItem item = Profile.getPlayerSkullWithInfo(friend);
                        String[] lores = new String[] {
                                "&f&l당신에게 친구 추가 요청을 보낸 " + friend.getName() + "님입니다.",
                                "&f&l좌클릭하여 친구의 프로필을 봅니다.",
                                "&f&l쉬프트 좌클릭하여 해당 친구 요청을 수락합니다.",
                                "&f&l쉬프트 우클릭하여 해당 친구 요청을 거절합니다.",
                                "&f&l────[ &b&l정보 &f&l]────"
                        };
                        lores = (String[]) ArrayUtils.addAll(lores, item.getOriginalLores());
                        item.setSimpleLore(lores);
                        inv2.setItem(i, item.getItemStack());
                    }
                    ((Player) sender).openInventory(inv2);
                    break;
            }
        } else {
            StringBuilder text = new StringBuilder().append(Prefix.CHAT.value).append(" &f&lFriend Command Help\n")
                    .append(Prefix.CHAT.value).append(" &f&l/friend : 이 도움말을 봅니다\n")
                    .append(Prefix.CHAT.value).append(" &f&l/friend 요청 (Player) : 해당 유저에게 친구 추가 요청을 보냅니다\n")
                    .append(Prefix.CHAT.value).append(" &f&l/friend 목록 : 당신의 친구의 목록을 보여줍니다\n")
                    .append(Prefix.CHAT.value).append(" &f&l/friend 요청목록 : 당신이 받은 친구 요청을 보여줍니다\n");
            sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
        }
        return true;
    }
}
