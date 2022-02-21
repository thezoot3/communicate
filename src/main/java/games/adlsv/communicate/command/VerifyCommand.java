package games.adlsv.communicate.command;

import games.adlsv.communicate.api.mongoDB.PlayerSocialCollections;
import games.adlsv.communicate.api.util.DataUtil;
import org.bson.Document;
import org.bson.conversions.Bson;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;

import java.util.*;

public class VerifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0].matches("[0-9a-zA-z]{6}")) {
            MongoCollection usersCollection = PlayerSocialCollections.Path.USERS.getCollection().getCollection();
            MongoCollection commCollection =  PlayerSocialCollections.Path.COMMUNICATE.getCollection().getCollection();
            Document doc = (Document) usersCollection.find(eq("connectcode", args[0])).first();
            try {
                doc.getBoolean("certified");
                Bson update = Updates.set("connectcode", ((Player) sender).getUniqueId().toString());
                Bson certUpdate = Updates.set("certified", true);
                usersCollection.findOneAndUpdate(eq("connectcode", args[0]), certUpdate);
                usersCollection.findOneAndUpdate(eq("connectcode", args[0]), update);
                Document insertDoc = new Document().append("id", DataUtil.getDiscordIdFromPlayer((Player) sender))
                        .append("prefix", new Document().append("prefixes", Collections.emptyList()).append("equipped", ""))
                        .append("lastJoin", new Date().toString())
                        .append("info", new Document().append("introduce", ""))
                        .append("friend", new Document().append("friends", Collections.emptyList()).append("requests", Collections.emptyList()))
                        .append("ignore", Collections.emptyList())
                        .append("firstJoin", new Date().toString());
                commCollection.insertOne(insertDoc);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &a&l성공&f&l하였습니다. &a&l인증&f&l 페이지를 새로고침해주세요"));
            } catch(Exception e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &c&l실패&f&l하였습니다. 이미 인증하였거나 회원가입 하지 않았습니다"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&l인증에 &c&l실패&f&l하였습니다. 올바르지 않은 코드 형식입니다"));
        }
        return true;
    }
}
