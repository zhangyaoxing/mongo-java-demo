package com.mongodb.yaoxing.demo.domain;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;

import org.bson.Document;

import java.util.Arrays;


/**
 * 聚合功能演示
 */
public class Aggregate extends MongoBase {
    public Aggregate(MongoClient client) {
        super(client, "demo");
    }

    /**
     * 按favourteColor聚合
     */
    public void AggregateByFavouriteColor() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);

        Block<Document> printBlock = new Block<Document>() {
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        /*
        按favouriteColor聚合. 等价脚本:
        db.Person.aggregate([
            {$unwind: "$favouriteColor"},
            {$group: {_id: "$favouriteColor", count: {$sum: 1}}
        ]);
        */
        coll.aggregate(Arrays.asList(
                // 按$favouriteColor展开数组
                Aggregates.unwind("$favouriteColor"),
                // _id为group使用的键，count为聚合函数结果字段，sum表示每次+1
                Aggregates.group("$favouriteColor", Accumulators.sum("count", 1))
        )).forEach(printBlock);
    }
}
