package games.adlsv.communicate.api.chatting;

import org.bukkit.ChatColor;

public enum Prefix {
    CHAT("&f&l[ &3&lSocial&f&l ]&f", ""),
    DM("&f&l〔&c&l귓속말〕&f", ""),
    CHATMODE_서버("&f&l〔&7&l서버&f&l〕&f", "&f&l모든 플레이어가 볼 수 있는 채널입니다."),
    CHATMODE_월드("&f&l〔&2&l월드&f&l〕&f", "&f&l같은 월드에 있는 플레이어만 볼 수 있는 채널입니다."),
    CHATMODE_로컬("&f&l〔&6&l로컬&f&l〕&f", "&f&l반경 100블록 내의 플레이어만 볼 수 있는 채널입니다."),
    CHATMODE_방송("&f&l〔&b&l방송&f&l〕&f", "&f&l내용을 강조할 수 있는 채널입니다 &b&l쿨타임&f&l은 1분입니다.");
    public final String value;
    public final String info;
    Prefix(String text, String info) {
        this.value = ChatColor.translateAlternateColorCodes('&', text);
        this.info = ChatColor.translateAlternateColorCodes('&', info);
    }
}
