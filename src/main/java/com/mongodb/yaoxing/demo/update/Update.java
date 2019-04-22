package com.mongodb.yaoxing.demo.update;
import com.github.javafaker.Faker;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.Utils;
import com.mongodb.yaoxing.demo.pojo.Person;
import com.mongodb.yaoxing.demo.query.Find;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 此类用于演示更新类功能。
 */
public class Update extends MongoBase {
    public Update(MongoClient client) {
        super(client);
    }

    /**
     * 更新单个数组元素
     */
    public void updateSingleElm() {
        Faker faker = new Faker();
        String color = faker.color().name();
        String toColor;
        // 保证每次随机生成的颜色是不一样的
        while((toColor = faker.color().name()) == color);
        System.out.println(String.format("====Updating first matched faviouriteColor from \"%s\" to \"%s\".",
                color, toColor));
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        /*
        更新数组中第一个匹配的`color`为`toColor`. 等价脚本:
        db.Person.update({favouriteColor: color}, {$set: {'favouriteColor.$': toColor}});
        */
        UpdateResult result = coll.updateMany(eq("favouriteColor", color),
                set("favouriteColor.$", toColor));
        System.out.println(String.format("====%d documents matched, %d documents updated.",
                result.getMatchedCount(), result.getModifiedCount()));
    }

    public void replaceOne() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);
        Document doc = coll.find().limit(1).first();
        doc.put("newField", "new value");
        coll.replaceOne(eq("_id", doc.get("_id")), doc);
        System.out.println("====Document replaced.");
        System.out.println(doc.toJson());
    }

    /**
     * 更新全部匹配的数组元素
     */
    public void updateAllMatched() {
        Faker faker = new Faker();
        String color = faker.color().name();
        String toColor;
        // 保证每次随机生成的颜色是不一样的
        while((toColor = faker.color().name()) == color);
        System.out.println(String.format("====Updating all matched faviouriteColor from \"%s\" to \"%s\".",
                color, toColor));
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        /*
        更新所有匹配的`color`为`toColor`. 等价脚本:
        db.Person.update({favouriteColor: color}, {$set: {'favouriteColor[]': toColor}});
        */
        UpdateResult result = coll.updateMany(eq("favouriteColor", color),
                set("favouriteColor[]", toColor));
        System.out.println(String.format("====%d documents matched, %d documents updated.",
                result.getMatchedCount(), result.getModifiedCount()));

        UpdateOptions options = new UpdateOptions();
        options.arrayFilters(Arrays.asList(new Document[] {new Document("elm.classifyId", "1"),}));
        options.upsert(true);
        Document inc = new Document("dailyClassification.20180605.$[elm].sum", -1)
                .append("monthlyClassification.201806.$[elm].sum", -1)
                .append("annualClassification.$[elm].sum", -1);
        coll.updateOne(and(eq("cusmNo", "00001"),
                eq("tranCat", "1"),
                eq("year", "2018")),
                inc,
                options);
    }

    /**
     * 演示Bulk的使用
     */
    public void bulkUpdate() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);
        MongoCursor<Document> people = coll.find().limit(10).iterator();
        List<WriteModel<Document>> ops = new ArrayList<WriteModel<Document>>();
        while(people.hasNext()) {
            Document person = people.next();
            ops.add(new UpdateOneModel<Document>(eq("_id", person.get("_id")),
                    inc("age", 1)));
            System.out.println(String.format("====Bulk updating age of %s from %s to %s",
                    person.getObjectId("_id"),
                    person.getInteger("age"),
                    person.getInteger("age") + 1));
        }

        coll.bulkWrite(ops, new BulkWriteOptions().ordered(false));
    }
    public static void main(String[] args) {
        // MongoDB连接字符串
        MongoClient client = Utils.getMongoClient();

        Update update = new Update(client);
        update.updateSingleElm();
        update.replaceOne();
        update.bulkUpdate();
    }
}
