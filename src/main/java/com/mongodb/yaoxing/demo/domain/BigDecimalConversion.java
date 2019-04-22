package com.mongodb.yaoxing.demo.domain;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.pojo.Product;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.yaoxing.demo.Utils.getConfig;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BigDecimalConversion extends MongoBase {
    public BigDecimalConversion(MongoClient client) {
        super(client);
    }

    public static void main(String[] args) {
        System.out.println("====This is a MongoDB demo!====");

        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString(getConfig().getProperty("CONN_STRING"));

        // 指定使用BigDecimal来解码Map中的BsonType.DECIMAL128
        Map<BsonType, Class<?>> replacements = new HashMap<BsonType, Class<?>>();
        replacements.put(BsonType.DECIMAL128, BigDecimal.class);
        BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap(replacements);
        MapCodecProvider mapProvider = new MapCodecProvider(bsonTypeClassMap);

        CodecRegistry pojoCodecRegistry = fromRegistries(
                fromProviders(mapProvider),
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connStr)
                .build();
        MongoClient client = MongoClients.create(settings);

        BigDecimalConversion conv = new BigDecimalConversion(client);
        conv.decimalConversion();

    }
    public void decimalConversion() {
        MongoCollection<Product> collection = this.getDefaultDatabase()
                .getCollection("Product", Product.class);
        HashMap map = new HashMap();
        map.put("price", BigDecimal.valueOf(19.99));
        Product product = new Product("0001", "MongoDB T-shirt", map);
        collection.insertOne(product);
        System.out.println("Product written to collection: " + product.toString());
        // 从集合中读取并映射为POJO
        Product newProduct = collection.find()
                .sort(new Document("_id", -1))
                .limit(1)
                .iterator()
                .next();
        Object price = newProduct.getVariables().get("price");
        System.out.println("Product BSON deserialize to POJO: " + price);
        System.out.println("Type of price is: " + price.getClass().getSimpleName());
    }
}