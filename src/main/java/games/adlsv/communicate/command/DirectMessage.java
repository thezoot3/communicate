package games.adlsv.communicate.command;

import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.util.DataUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DirectMessage implements CommandExecutor {
    public static HashMap<Player, OfflinePlayer> dm = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        StringBuilder message = new StringBuilder().append(Prefix.CHAT.value);
        if(args.length != 0 && DataUtil.isPlayerExist(args[0])) {
            OfflinePlayer oppPlayer = DataUtil.getPlayerFromString(args[0]);
            if(dm.get((Player) sender) != null) {
                message.append(" &f&l").append(dm.get((Player) sender).getName()).append("님과의 DM모드를 종료했습니다");
                dm.remove((Player) sender);
                sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message.toString())));
            } else if(dm.get((Player) sender) != oppPlayer && dm.get((Player) sender) != null) {
                message.append(" &f&l").append(dm.get((Player) sender).getName()).append("님과의 DM모드를 종료하고\n")
                        .append(Prefix.CHAT.value).append(" &f&l").append(oppPlayer.getName()).append("님과의 DM모드를 시작했습니다");
                dm.remove((Player) sender);
                dm.put((Player) sender, oppPlayer);
                sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message.toString())));
            } else if(dm.get((Player) sender) == null) {
                message.append(" &f&l").append(oppPlayer.getName()).append("님과의 DM모드를 시작했습니다");
                dm.put((Player) sender, oppPlayer);
                sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message.toString())));
            }
        } else {
            message.append(" &f&l/귓속말 (Player) : 해당 유저와 1:1 대화를 시작하거나 종료합니다.\n");
            message.append(Prefix.CHAT.value).append(" &f&l또한 다른 채팅 모드를 시작함으로써 DM모드를 종료할 수 있습니다");
            sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', message.toString())));
        }
        return true;
    }
}
