package com.mongodb.yaoxing.demo.domain;

import com.github.javafaker.Faker;
import com.mongodb.client.*;
import com.mongodb.client.model.Projections;
import com.mongodb.yaoxing.demo.pojo.Person;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

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
    public void FindSingleArrayElm() {
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
}
