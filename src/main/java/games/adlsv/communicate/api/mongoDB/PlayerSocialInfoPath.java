package games.adlsv.communicate.api.mongoDB;

import games.adlsv.mongoDBAPI.Enum.EnumDocumentPath;
import games.adlsv.mongoDBAPI.MongoDBDocument;
import games.adlsv.mongoDBAPI.MongoDBDocumentPath;

public class PlayerSocialInfoPath implements EnumDocumentPath {
    public enum Path {
        PREFIXES("prefix.prefixes", PlayerSocialCollections.Path.COMMUNICATE),
        EQUIPPED("prefix.equipped", PlayerSocialCollections.Path.COMMUNICATE),
        IGNORES("ignore", PlayerSocialCollections.Path.COMMUNICATE),
        FRIENDS("friend.friends", PlayerSocialCollections.Path.COMMUNICATE),
        FRIENDS_REQUEST("friend.requests", PlayerSocialCollections.Path.COMMUNICATE),
        LASTJOIN("lastJoin", PlayerSocialCollections.Path.COMMUNICATE),
        INTRODUCE("info.introduce", PlayerSocialCollections.Path.COMMUNICATE),
        FIRSTJOIN("firstJoin", PlayerSocialCollections.Path.COMMUNICATE),
        CERTIFIED("certified", PlayerSocialCollections.Path.USERS);
        Path(String path, PlayerSocialCollections.Path collections) {
            dpath = new MongoDBDocumentPath(path, collections.getCollection());
        }
        private final MongoDBDocumentPath dpath;
        public MongoDBDocumentPath getPath() {
            return dpath;
        }
    }
}

