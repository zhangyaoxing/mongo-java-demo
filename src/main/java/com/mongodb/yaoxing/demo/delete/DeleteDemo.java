package com.mongodb.yaoxing.demo.delete;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.Utils;
import com.mongodb.yaoxing.demo.pojo.Person;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;

/**
 * 此类用于演示更新类功能。
 */
public class DeleteDemo extends MongoBase {
    public DeleteDemo(MongoClient client) {
        super(client);
    }

    /**
     * 更新全部匹配的数组元素
     */
    public void deleteMany() {
        Faker faker = new Faker();
        String color = faker.color().name();
        System.out.println(String.format("====Remove documents whose favouriteColor is \"%s\" .", color));
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        /*
        删除所有匹配的`color`. 等价脚本:
        db.Person.deleteMany({favouriteColor: color});
        */
        DeleteResult result = coll.deleteMany(eq("favouriteColor", color));
        System.out.println(String.format("====%d documents matched and removed.", result.getDeletedCount()));
    }

    public static void main(String[] args) {
        // MongoDB连接字符串
        MongoClient client = Utils.getMongoClient();

        DeleteDemo delete = new DeleteDemo(client);
        delete.deleteMany();
    }
}
