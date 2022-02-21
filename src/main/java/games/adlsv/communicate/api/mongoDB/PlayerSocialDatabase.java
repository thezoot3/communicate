package games.adlsv.communicate.api.mongoDB;

import games.adlsv.mongoDBAPI.Enum.EnumDatabase;
import games.adlsv.mongoDBAPI.MongoDBConnection;
import games.adlsv.mongoDBAPI.MongoDBDatabase;

import static games.adlsv.communicate.Communicate.connection;

public class PlayerSocialDatabase implements EnumDatabase {
    enum Path {
        MYFIRSTDATABASE("myFirstDatabase", connection);
        Path(String dbname, MongoDBConnection connection) {
            db = new MongoDBDatabase(connection, dbname);
        }
        private final MongoDBDatabase db;
        public MongoDBDatabase getDatabase() {
            return db;
        }
    }
}
