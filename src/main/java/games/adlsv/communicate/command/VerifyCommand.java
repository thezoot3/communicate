package games.adlsv.communicate.command;

import games.adlsv.communicate.api.util.dataUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import games.adlsv.communicate.api.mongoDB.MongoDBCollections;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import java.util.Date;
import java.util.Objects;

public class VerifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0].matches("[0-9a-zA-z]{6}")) {
            MongoCollection collection = MongoDBCollections.USERS.getCollection();
            Document doc = (Document) collection.find(eq("connectcode", args[0])).first();
            try {
                Objects.requireNonNull(doc).getBoolean("certified");
                Bson update = Updates.set("connectcode", ((Player) sender).getUniqueId().toString());
                collection.findOneAndUpdate(eq("connectcode", args[0]), update);
                Document insertDoc = new Document().append("id", dataUtil.getDiscordIdFromPlayer((Player) sender))
                        .append("prefix.prefixes", new String[]{})
                        .append("prefix.equipped", "")
                        .append("lastJoin", new Date().toString())
                        .append("info.introduce", "")
                        .append("friend.friends", new String[]{})
                        .append("friend.requests", new String[]{})
                        .append("ignore", new String[]{});
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &a&l성공&f&l하였습니다. &a&l인증&f&l 페이지를 새로고침해주세요"));
            } catch(NullPointerException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &c&l실패&f&l하였습니다. 이미 인증하였습니다"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &c&l실패&f&l하였습니다. 올바르지 않은 코드 형식입니다"));
        }
        return true;
    }
}
