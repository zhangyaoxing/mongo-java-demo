import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.yaoxing.demo.domain.Generator;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Main {
    public static void main(String[] args) {
        System.out.print("This is a MongoDB demo!\n");
        ConnectionString connStr = new ConnectionString("mongodb://127.0.0.1/demo");

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connStr)
                .build();
        MongoClient client = MongoClients.create(settings);

        // Generate sample data x10000
        Generator gen = new Generator(client);
        gen.genData();
    }
}