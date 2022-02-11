package games.adlsv.communicate.api.mongoDB;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Objects;

public class MongoDBDocument {
    public JsonObject element;
    private JsonParser parser = new JsonParser();
    public MongoDBDocument(MongoDBCollections collection, Bson filter) {
        Document docu = (Document) collection.getCollection().find(filter).first();
        if(docu != null) {
            String str = docu.toJson();
            element = parser.parse(str).getAsJsonObject();
        } else {
            element = null   ;
        }
    }
    /**
     * JSON 도큐먼트에서 값을 찾는 메소드
     * ".."을 통해 중첩된 구문을 찾을 수 있음 "parents..children"
     *
     * @param key 요소를 찾기 위한 키
     */
    public JsonElement get(String key) {
        String[] keys = key.contains(".") ? key.split("[.]{1}") : new String[]{key};
        JsonObject obj = element;
        for(String s: keys) {
            if (obj.get(s) != null) {
                try {
                    obj = obj.get(s).getAsJsonObject();
                } catch (IllegalStateException e) { //JSON OBJECT가 아닌 원하는걸 찾았을때 익셉션이 나옴
                    return obj.get(s);
                }
            } else {
                return null;
            }
        }
        return obj;
    }
}
