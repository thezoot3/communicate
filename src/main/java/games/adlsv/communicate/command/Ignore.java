package games.adlsv.communicate.command;

import com.google.gson.JsonArray;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.social.Ignores;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.communicate.api.util.SimpleItem;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Ignore implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0) {
            switch (args[0]) {
                case "추가":
                    if(args.length != 1&&DataUtil.isPlayerExist(args[1])) {
                        Ignores ign = new Ignores((Player) sender);
                        ign.addIgnores((Bukkit.getOfflinePlayer(args[1])));
                    }
                    break;
                case "제거":
                    if (args.length != 1&&DataUtil.isPlayerExist(args[1])) {
                        Ignores ign = new Ignores((Player) sender);
                        ign.removeIgnores((Bukkit.getOfflinePlayer(args[1])));
                    }
                    break;
                case "목록":
                    Inventory inv = Bukkit.createInventory((InventoryHolder) sender, 54, Component.text("차단 목록"));
                    PlayerSocialInfo info = new PlayerSocialInfo((Player) sender);
                    JsonArray fList = info.get(PlayerSocialInfo.pathList.IGNORES).getAsJsonArray();
                    for (int i = 0; i < fList.size(); i++) {
                        OfflinePlayer ignores = Bukkit.getOfflinePlayer(UUID.fromString(fList.get(i).getAsString()));
                        SimpleItem item = Profile.getPlayerSkullWithInfo(ignores);
                        String[] lores = new String[]{
                                "&f&l당신이 차단한 플레이어인 " + ignores.getName() + "님입니다.",
                                "&f&l좌클릭하여 친구의 프로필을 봅니다.",
                                "&f&l우클릭하여 해당 유저의 차단을 해제합니다.",
                                "&f&l────[ &b&l정보 &f&l]────"
                        };
                        lores = (String[]) ArrayUtils.addAll(lores, item.getOriginalLores());
                        item.setSimpleLore(lores);
                        inv.setItem(i, item.getItemStack());
                    }
                    ((Player) sender).openInventory(inv);
                    break;
            }
        } else {
            StringBuilder text = new StringBuilder().append(Prefix.CHAT.value).append(" &f&lIgnore Command Help\n")
                    .append(Prefix.CHAT.value).append(" &f&l/ignore: 이 도움말을 봅니다\n")
                    .append(Prefix.CHAT.value).append(" &f&l/ignore 추가 (Player) : 해당 유저를 차단합니다.\n")
                    .append(Prefix.CHAT.value).append(" &f&l/ignore 제거 (Player) : 해당 유저를 차단 해제합니다.\n")
                    .append(Prefix.CHAT.value).append(" &f&l/ignore 목록 : 당신이 차단한 유저의 목록을 보여줍니다\n");
            sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
        }
        return true;
    }
}
