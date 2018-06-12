import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.yaoxing.demo.domain.Aggregate;
import com.mongodb.yaoxing.demo.domain.Find;
import com.mongodb.yaoxing.demo.domain.Update;
import com.mongodb.yaoxing.demo.domain.Insert;
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
        Insert gen = new Insert(client);
        gen.cleanup();
        gen.insertData();

        // Update array element
        Update update = new Update(client);
        update.updateSingleElm();
        update.updateAllMatched();

        // Run aggregation
        Aggregate agg = new Aggregate(client);
        agg.AggregateByFavouriteColor();

        // Run array query
        Find f = new Find(client);
        f.FindSingleArrayElm();
    }
}