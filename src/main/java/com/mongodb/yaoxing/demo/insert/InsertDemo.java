package com.mongodb.yaoxing.demo.insert;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import com.github.javafaker.Faker;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.Utils;
import com.mongodb.yaoxing.demo.pojo.Person;
import com.mongodb.yaoxing.demo.pojo.Phone;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 此类用于演示如何插入数据。
 */
public class InsertDemo extends MongoBase {
    public static final int BATCH_SIZE = 100;
    public static final int TOTAL_COUNT = 10000;
    public static final int ARRAY_LEN = 5;
    public static final String[] PHONE_TYPE = new String[] {"home", "work", "cell"};
    public InsertDemo(MongoClient client) {
        super(client);
    }

    /**
     * 清理工作
     */
    public void cleanup() {
        MongoDatabase db = this.getDefaultDatabase();

        // 删除目标表
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);
        coll.drop();
        MongoCollection out = db.getCollection("out");
        out.drop();
        System.out.println(String.format("====Collection %s, %s dropped!", "Person", "out"));

        // 在目标表上创建索引（后续其他操作将会使用）
        coll.createIndex(eq("favouriteColor", 1));
        System.out.println(String.format("====Index created on field '%s'!", "favouriteColor"));
    }

    /**
     * 批量插入数据（Document版本）。这些数据将为其他演示功能使用。
     */
    public void insertDataDocument() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);
        Faker faker = new Faker();
        List<Document> data = new ArrayList<Document>();
        for(int i = 0; i < TOTAL_COUNT; i++) {
            // 每条数据中含有ARRAY_LEN数量的颜色数组
            List<String> colors = new ArrayList<String>();
            for (int j = 0; j < ARRAY_LEN; j++) {
                colors.add(faker.color().name());
            }

            Date bDay = faker.date().birthday();
            int age = (int) ((new Date().getTime() - bDay.getTime()) / 3600 / 24 / 365 / 1000);

            List<Document> phones = new ArrayList<Document>();
            for (int j = 0; j < PHONE_TYPE.length; j++) {
                Document phone = new Document("type", PHONE_TYPE[j]);
                phone.append("number", faker.phoneNumber().phoneNumber());
                phones.add(phone);
            }
            Document person = new Document();
            person.put("name", faker.name().fullName());
            person.put("address", faker.address().fullAddress());
            person.put("birthday", bDay);
            person.put("favouriteColor", colors);
            person.put("age", age);
            person.put("amount", new BigDecimal(faker.number().numberBetween(10, 100)));
            person.put("phones", phones);
            data.add(person);
            // 使用批量方式插入以提高效率
            if (i % BATCH_SIZE == 0) {
                coll.insertMany(data);
                data.clear();
            }
        }
        if (data.size() > 0) {
            coll.insertMany(data);
        }
        System.out.println(String.format("====%d documents generated!", TOTAL_COUNT));
    }

    public static void main(String[] args) {
        MongoClient client = Utils.getMongoClient();

        InsertDemo insert = new InsertDemo(client);
        insert.insertDataDocument();
    }
}
