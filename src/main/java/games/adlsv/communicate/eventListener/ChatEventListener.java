package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.command.ChatMode;
import games.adlsv.communicate.command.DirectMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;

import games.adlsv.communicate.api.chatting.PlayerSocialInfo;

public class ChatEventListener implements Listener { ;
    @EventHandler (priority = EventPriority.HIGH)
    public void AsyncChatEvent(AsyncChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        String message = ((TextComponent) e.message()).content();
        if(ChatMode.chatmode.get(p) != null) {
            Bukkit.broadcast(Component.text(getFomattedMessage(
                    message,
                    p,
                    ChatMode.chatmode.get(p).toString()
            )));
        } else if(DirectMessage.dm.get(p) != null) {
            Player offPlayer = DirectMessage.dm.get(p);
            Component msg = Component.text(getFomattedMessage(
                    message,
                    p,
                    Prefix.DM.value
            ));
            if(Bukkit.getPlayer(offPlayer.getName()) != null) { // 접속 중인 플레이언지 확인 (안하면 널포인터 :O )
                offPlayer.sendMessage(msg);
            }
            p.sendMessage(msg);
        } else {
            String msg = Prefix.CHAT.value + " &f&l채팅 채널을 선택하지 않으셨습니다.\n" + Prefix.CHAT.value + " &f'&c&l/채팅모드, /chatmode&f'&f&l를 입력해 채팅모드를 설정하세요";
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }
    private static String getFomattedMessage(String msg, Player sender, String chatmodeValue) {
        StringBuilder text = new StringBuilder();
        try {
            PlayerSocialInfo info = new PlayerSocialInfo(sender);
            String prefix = info.get(PlayerSocialInfo.pathList.EQUIPPED).getAsString().replace("\"", "");
            text.append(chatmodeValue) //채팅 모드 적용
                    .append((prefix != null) ? prefix + " " : "") //칭호 적용
                    .append((sender.isOp()) ? ChatColor.DARK_RED + sender.getName() + ChatColor.RESET : sender.getName()) //OP이면 빨간색
                    .append(": ")
                    .append(msg); // 메시지는 Component임 Component -> TextComponent에서 content 뽑아오기
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', text.toString()))
                    .clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/profile " + sender.getName())
                    )
                    .hoverEvent(HoverEvent.showText(
                                    Component.text(ChatColor.translateAlternateColorCodes('&', "&f&l메시지를 클릭하여 프로필을 확인하세요"))
                            )
                    );
            return message.toString();
        } catch (Exception ignored) {
            return null;
        }
    }
}
