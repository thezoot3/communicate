package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.api.social.Ignores;
import games.adlsv.communicate.command.ChatMode;
import games.adlsv.communicate.command.DirectMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;

import games.adlsv.communicate.api.chatting.PlayerSocialInfo;

import java.util.Objects;

public class ChatEventListener implements Listener { ;
    @EventHandler (priority = EventPriority.HIGH)
    public void AsyncChatEvent(AsyncChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        String message = ((TextComponent) e.message()).content();
        if(ChatMode.chatmode.get(p) != null) {
            Component fm = getFomattedMessage(message, p, ChatMode.chatmode.get(p).value);
            for(Player ps: Bukkit.getOnlinePlayers()) {
                Ignores ign = new Ignores(ps);
                if(!ign.isIgnores(p)) {
                    ps.sendMessage(fm);
                }
            }
            Bukkit.getLogger().info(((TextComponent) fm).content());
        } else if(DirectMessage.dm.get(p) != null) {
            OfflinePlayer offPlayer = DirectMessage.dm.get(p);
            Component fm = getFomattedMessage(message, p, Prefix.DM.value);
            if(offPlayer.getPlayer() != null && new Ignores(offPlayer.getPlayer()).isIgnores(p)) { // 접속 중인 플레이언지 확인 (안하면 널포인터 :O )
                offPlayer.getPlayer().sendMessage(fm);
            }
            p.sendMessage(fm);
            Bukkit.getLogger().info(((TextComponent) fm).content());
        } else {
            String msg = Prefix.CHAT.value + " &f&l채팅 채널을 선택하지 않으셨습니다.\n" + Prefix.CHAT.value + " &f'&c&l/채팅모드, /chatmode&f'&f&l를 입력해 채팅모드를 설정하세요";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
    private static Component getFomattedMessage(String msg, Player sender, String chatmodeValue) {
        StringBuilder text = new StringBuilder();
        try {
            PlayerSocialInfo info = new PlayerSocialInfo(sender);
            String prefix = info.get(PlayerSocialInfo.pathList.EQUIPPED).getAsString().replace("\"", "");
            text.append(chatmodeValue) //채팅 모드 적용
                    .append((prefix != null) ? " " + prefix + " " : "") //칭호 적용
                    .append((sender.isOp()) ? ChatColor.DARK_RED + sender.getName() + ChatColor.RESET : sender.getName()) //OP이면 빨간색
                    .append(": ")
                    .append(msg);
            return Component.text(ChatColor.translateAlternateColorCodes('&', text.toString()))
                    .clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/profile " + sender.getName())
                    )
                    .hoverEvent(HoverEvent.showText(
                                    Component.text(ChatColor.translateAlternateColorCodes('&', "&f&l메시지를 클릭하여 프로필을 확인하세요"))
                            )
                    );
        } catch (Exception ig) {
            ig.printStackTrace();
            return Component.text("");
        }
    }
}
