import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.yaoxing.demo.domain.Aggregate;
import com.mongodb.yaoxing.demo.domain.ArrayUpdate;
import com.mongodb.yaoxing.demo.domain.Generator;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.PojoCodecProvider;

public class Main {
    public static void main(String[] args) {
        System.out.println("====This is a MongoDB demo!====");
        ConnectionString connStr = new ConnectionString("mongodb://127.0.0.1/demo");

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connStr)
                .build();
        MongoClient client = MongoClients.create(settings);

        // Cleanup and generate sample data x10000
        Generator gen = new Generator(client);
        gen.cleanup();
        gen.genData();

        // Update array element
        ArrayUpdate update = new ArrayUpdate(client);
        update.updateSingleElm();
        update.updateAllMatched();

        // Run aggregation
        Aggregate agg = new Aggregate(client);
        agg.AggregateByFavouriteColor();
    }
}