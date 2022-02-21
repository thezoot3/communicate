package games.adlsv.communicate.api.mongoDB;

import games.adlsv.mongoDBAPI.Enum.EnumCollections;
import games.adlsv.mongoDBAPI.MongoDBCollections;

public class PlayerSocialCollections implements EnumCollections {
    public enum Path {
        USERS(PlayerSocialDatabase.Path.MYFIRSTDATABASE, "users"),
        COMMUNICATE(PlayerSocialDatabase.Path.MYFIRSTDATABASE, "commuicate");
        Path(PlayerSocialDatabase.Path database, String collection) {
            this.collection = new MongoDBCollections(database.getDatabase(), collection);
        }
        private final MongoDBCollections collection;
        public MongoDBCollections getCollection() {
            return collection;
        }
    }

}
