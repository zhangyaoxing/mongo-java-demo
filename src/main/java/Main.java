import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.yaoxing.demo.domain.*;
import org.bson.BsonReader;
import org.bson.Document;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.json.Converter;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;

public class Main {
    public static void main(String[] args) {
        System.out.println("====This is a MongoDB demo!====");

        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString("mongodb://127.0.0.1/demo");

        // 用于转换POJO与BSON的转换器。如果不使用POJO则不需要调用
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        // 使用连接字符串，POJO转换器生成一个MongoDB配置
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connStr)
                .build();

        MongoClient client = MongoClients.create(settings);


//        // 清理测试集合并生成测试数据10000条
//        Insert gen = new Insert(client);
//        gen.cleanup();
//        gen.insertData();
//        gen.insertDataDocument();
//
//        // 更新数组元素
        Update update = new Update(client);
//        update.updateSingleElm();
//        update.updateAllMatched();
//        update.bulkUpdate();
        update.replaceOne();
//
//        // 查询数组元素
//        Find f = new Find(client);
//        f.findSingleArrayElm();
//        f.findArray();
//
//        // 执行聚合
//        Aggregate agg = new Aggregate(client);
//        agg.AggregateByFavouriteColor();
//
//        // 调用Spark Connector
//        Spark spark = new Spark();
//        spark.groupByBirthday();
//        JsonWriterSettings settings = JsonWriterSettings.builder()
//                .int32Converter(new Converter<Integer>() {
//                    public void convert(Integer value, StrictJsonWriter writer) {
//                        writer.writeNumber(value.toString());
//                    }
//                })
//                .build();
//
//        int v = 10;
        Document d = Document.parse("{a: 100}");
    }
}