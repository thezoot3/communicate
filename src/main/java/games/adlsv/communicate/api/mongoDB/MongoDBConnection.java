package games.adlsv.communicate.api.mongoDB;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
public class MongoDBConnection {
    public MongoClient client;
    public MongoDBConnection() {
            String passwd = "koreayonsei5813";
            String uri = "mongodb+srv://thezoot3:"+ passwd +"@arendell.vyzh8.mongodb.net";
            this.client = MongoClients.create(uri);
    }
    public static final MongoDBConnection connection = new MongoDBConnection();
}
