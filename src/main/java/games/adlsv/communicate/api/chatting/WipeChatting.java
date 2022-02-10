package games.adlsv.communicate.api.chatting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.Collection;

public class WipeChatting {
    public static void wipeAll() {
        Collection<? extends Player> plist = Bukkit.getOnlinePlayers();
        for(Player p: plist) {
            for(int i = 0; i < 70; i++) {
                p.sendMessage("");
            }
        }
    }
    public static void wipe(Player p) {
        for(int i = 0; i < 70; i++) {
            p.sendMessage("");
        }
    }
}
