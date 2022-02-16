package games.adlsv.communicate.command;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import com.mongodb.client.model.Updates;
import games.adlsv.communicate.api.chatting.PlayerSocialInfo;
import games.adlsv.communicate.api.chatting.Prefix;

import games.adlsv.communicate.api.mongoDB.MongoDBCollections;
import games.adlsv.communicate.api.util.DataUtil;
import net.kyori.adventure.text.Component;

import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class PlayerPrefix implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PlayerSocialInfo ps = new PlayerSocialInfo((Player) sender);
        JsonElement element = ps.get(PlayerSocialInfo.pathList.PREFIXES);
        ArrayList<String> list = new Gson().fromJson(element.getAsJsonArray().toString(), ArrayList.class);
        StringBuilder text = new StringBuilder().append(Prefix.CHAT.value);
        switch(args.length){
            case 1:
                if(args[0].equals("list")||args[0].equals("목록")) {
                    try {
                        text.append(" &a&l")
                                .append(sender.getName())
                                .append("&f&l님이 보유하신 칭호");
                        for(int i = 0; i<list.size();i++) {
                            text.append("\n")
                                    .append(Prefix.CHAT.value)
                                    .append(" &f&l")
                                    .append(i + 1)
                                    .append("&f. ")
                                    .append(list.get(i));
                        }
                        sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                break;
            case 2:
                if(args[0].equals("equip")||args[0].equals("착용")) {
                    try {
                        String prefix = list.get(Integer.parseInt(args[1]) - 1);
                        if(prefix != null) {
                            Bson updates = Updates.set("prefix.equipped", prefix);
                            MongoDBCollections.COMMUNICATE.getCollection().findOneAndUpdate(eq("id", DataUtil.getDiscordIdFromPlayer((Player) sender)), updates);
                            text.append(" \"")
                                    .append(prefix)
                                    .append("\" &f&l칭호를 착용하셨습니다.");
                            sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
                        }
                    } catch(IndexOutOfBoundsException e) {
                        text.append(" &f&l존재하지 않는 칭호입니다");
                        sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
                    }
                    return true;
                } else if(args[0].equals("unequip")||args[0].equals("착용해제")) {
                    String prefix = ps.get(PlayerSocialInfo.pathList.EQUIPPED).getAsString();
                    if (prefix != null) {
                        Bson updates = Updates.set("prefix.equipped", "");
                        MongoDBCollections.COMMUNICATE.getCollection().findOneAndUpdate(eq("id", DataUtil.getDiscordIdFromPlayer((Player) sender)), updates);
                        text.append(" \"")
                                .append(prefix)
                                .append("\" &f&l칭호를 착용 해제하셨습니다.");
                        sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
                    } else {
                        text.append(" &f&l칭호를 착용하고 있지 않습니다.");
                        sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
                    }
                }
            case 0:
                text.append(" &f&l칭호(Prefix) 명령어 도움말\n")
                        .append(Prefix.CHAT.value).append(" &f&l/칭호 : 명령어 도움말을 봅니다.\n")
                        .append(Prefix.CHAT.value).append(" &f&l/칭호 착용 (번호) : 해당 번호에 해당하는 칭호를 착용합니다.\n")
                        .append(Prefix.CHAT.value).append(" &f&l/칭호 착용해제 (번호) : 해당 번호에 해당하는 칭호를 착용 해제합니다.");
                sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', text.toString())));
        }
        return true;
    }
}
