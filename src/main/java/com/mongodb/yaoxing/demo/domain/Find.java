package com.mongodb.yaoxing.demo.domain;

import com.github.javafaker.Faker;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.yaoxing.demo.MongoBase;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * 此类用于演示查询相关操作。
 */
public class Find extends MongoBase {
    public Find(MongoClient client) {
        super(client);
    }

    /**
     * 根据条件查询数组元素并返回找到的元素
     */
    public void findSingleArrayElm() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);
        // 随机生成要查询的颜色
        Faker faker = new Faker();
        String color = faker.color().name();
        System.out.println(String.format("====We are querying: %s", color));

        MongoCursor<Document> people = coll.find(eq("favouriteColor", color))
                // favouriteColor.$代表匹配到的数组元素（注意$只能匹配一个元素）
                .projection(Projections.include("favouriteColor.$", "name"))
                .limit(10).iterator();
        System.out.println("====First 10 found results:");

        while(people.hasNext()) {
            Document doc = people.next();
            System.out.println(doc.toJson());
        }
    }

    /**
     * 如何使用一个数组及数组中的子文档
     */
    public void findArray() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);
        MongoCursor<Document> people = coll.find().limit(10).iterator();
        System.out.println("====First 10 found results:");

        while(people.hasNext()) {
            Document doc = people.next();
            // 数组在Document中的类型实际是ArrayList
            List<String> colors = (List<String>) doc.get("favouriteColor");
            List<Document> phones = (List<Document>) doc.get("phones");
            System.out.println(String.format("====Favourite colors: %s", colors));
            for(int i = 0; i < phones.size(); i++) {
                Document phone = phones.get(i);
                // 通过getString可以将Document中的value以String形式取出
                String type = phone.getString("type");
                String number = phone.getString("number");
                System.out.println(String.format("====Phone numbers: %s %s", type, number));
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("====This is a MongoDB demo!====");

        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString("mongodb://127.0.0.1:29018/demo?slaveOk=true");
        MongoClient client = MongoClients.create(connStr);

        Find find = new Find(client);
        for (int i = 0; i < 10000; i++) {
            find.findSingleArrayElm();
        }
    }
}
