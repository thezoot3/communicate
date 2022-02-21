package games.adlsv.communicate.command;

import com.google.gson.JsonElement;
import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.mongoDB.PlayerSocialInfoPath;
import games.adlsv.communicate.api.util.DataUtil;
import games.adlsv.communicate.api.util.SimpleItem;
import games.adlsv.mongoDBAPI.MongoDBDocumentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import static com.mongodb.client.model.Filters.eq;

public class Profile implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0 && DataUtil.isPlayerExist(args[0])) {
            openProfile((Player) sender, args[0]);
        } else {
            openProfile((Player) sender, sender.getName());
        }
        return true;
    }
    public static void openProfile(Player toShow, String owner) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(owner);
        Inventory inv = Bukkit.createInventory(
                toShow, 54,
                Component.text(ChatColor.translateAlternateColorCodes('&', ChatColor.BOLD + owner + "님의 프로필"))
        );
        MongoDBDocumentUtil info = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
        for(int i = 0; i < 6; i++) {
            inv.setItem(i * 9, new ItemStack(Material.GREEN_STAINED_GLASS));
            inv.setItem(i * 9 + 8, new ItemStack(Material.GREEN_STAINED_GLASS));
        }
        SimpleItem dmIcon = new SimpleItem(Material.OAK_SIGN, "&6&l" + p.getName() + "&f&l님과 DM 시작하기", new String[]{
                "&f&l클릭 시 이 유저와 DM을 시작합니다.",
                "&f&lDM은 다른 유저에게 노출되지 않습니다."
        });
        SimpleItem friendIcon = new SimpleItem(Material.EMERALD, "&6&l" + p.getName() + "&f&l님에게 친구 요청 보내기");
        for(JsonElement e : info.get(PlayerSocialInfoPath.Path.FRIENDS.getPath()).getAsJsonArray()) {
            if(e.getAsString().equals(toShow.getName())) {
                friendIcon.setName("&6&l" + p.getName() + "&f&l님은 당신의 친구입니다!");
            }
        }
        SimpleItem ignoreIcon = new SimpleItem(Material.BARRIER, "&6&l" + p.getName() + "&f&l님을 &4&l차단&f&l하기");
        for(JsonElement e : info.get(PlayerSocialInfoPath.Path.IGNORES.getPath()).getAsJsonArray()) {
            if(e.getAsString().equals(toShow.getName())) {
                friendIcon.setName("&6&l" + p.getName() + "&f&l님은 이미 &4&l차단&f&l 된 상태입니다.");
            }
        }
        inv.setItem(29, dmIcon.getItemStack());
        inv.setItem(31, friendIcon.getItemStack());
        inv.setItem(33, ignoreIcon.getItemStack());
        inv.setItem(4, getPlayerSkullWithInfo(Bukkit.getOfflinePlayer(p.getUniqueId())).getItemStack());
        toShow.openInventory(inv);
    }
    public static SimpleItem getPlayerSkullWithInfo(OfflinePlayer p) {
        MongoDBDocumentUtil info = new MongoDBDocumentUtil( PlayerSocialCollections.Path.COMMUNICATE.getCollection(), eq("id", DataUtil.getDiscordIdFromPlayer(p)));
        SimpleItem playerHead = new SimpleItem(Material.PLAYER_HEAD, ChatColor.BOLD + p.getName() + "님의 프로필");
        SkullMeta meta = ((SkullMeta) playerHead.getItemMeta());
        meta.setOwningPlayer(p);
        playerHead.setItemMeta(meta);
        String[] lore = new String[]{
                new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(
                        p.isOnline() ?
                                "현재 접속 중인 플레이어입니다" :
                                "마지막 접속 시각: " + info.get(PlayerSocialInfoPath.Path.LASTJOIN.getPath()).getAsString()
                ).toString(),
                new StringBuilder().append(ChatColor.WHITE).append(ChatColor.BOLD).append(
                        info.get(PlayerSocialInfoPath.Path.INTRODUCE.getPath()).getAsString() != null && !info.get(PlayerSocialInfoPath.Path.INTRODUCE.getPath()).getAsString().equals("")?
                                "소개: " + info.get(PlayerSocialInfoPath.Path.INTRODUCE.getPath()).getAsString() :
                                "소개가 없습니다"
                ).toString()
        };;
        playerHead.setSimpleLore(lore);
        return playerHead;
    }
}
