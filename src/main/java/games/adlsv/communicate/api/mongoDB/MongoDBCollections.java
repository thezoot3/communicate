package games.adlsv.communicate.api.mongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public enum MongoDBCollections {
    USERS("users"),
    COMMUNICATE("commuicate");
    private final MongoCollection collection;
    MongoDBCollections(String collectionName) {
        String dataBase = "myFirstDatabase";
        MongoDatabase database= MongoDBConnection.connection.client.getDatabase(dataBase);
        this.collection = database.getCollection(collectionName);
    }
    public MongoCollection getCollection() {
        return this.collection;
    }
}
