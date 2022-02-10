package games.adlsv.communicate.eventListener;

import games.adlsv.communicate.api.chatting.Prefix;
import games.adlsv.communicate.command.ChatMode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;

import games.adlsv.communicate.api.chatting.PlayerSocialInfo;

public class ChatEventListener implements Listener { ;
    @EventHandler (priority = EventPriority.HIGH)
    public void AsyncChatEvent(AsyncChatEvent e) {
        e.setCancelled(true);
        if(ChatMode.chatmode.get(e.getPlayer()) != null) {
            StringBuilder text = new StringBuilder();
            try {
                PlayerSocialInfo info = new PlayerSocialInfo(e.getPlayer());
                String prefix = info.get(PlayerSocialInfo.pathList.EQUIPPED).getAsString().replace("\"", "");
                text.append(ChatMode.chatmode.get(e.getPlayer()).value) //채팅 모드 적용
                    .append((prefix != null) ? prefix + " " : "") //칭호 적용
                    .append((e.getPlayer().isOp()) ? ChatColor.DARK_RED + e.getPlayer().getName() + ChatColor.RESET : e.getPlayer().getName()) //OP이면 빨간색
                    .append(": ")
                    .append(((TextComponent) e.message()).content()); // 메시지는 Component임 Component -> TextComponent에서 content 뽑아오기
                Component message = Component.text(ChatColor.translateAlternateColorCodes('&', text.toString()))
                        .clickEvent(ClickEvent.clickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                "/profile " + e.getPlayer().getName())
                        )
                        .hoverEvent(HoverEvent.showText(
                                Component.text(ChatColor.translateAlternateColorCodes('&', "&f&l메시지를 클릭하여 프로필을 확인하세요"))
                        )
                );
                Bukkit.broadcast(message);
            } catch(Exception ignored) {

            }
        } else {
            TextComponent message =  Component.text(Prefix.CHAT.value + " &f&l채팅 채널을 선택하지 않으셨습니다.\n" + Prefix.CHAT.value + " &f'&c&l/채팅모드, /chatmode&f'&f&l를 입력해 채팅모드를 설정하세요");
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message.content()));
        }
    }
}
