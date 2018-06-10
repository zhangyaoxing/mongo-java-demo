package com.mongodb.yaoxing.demo.domain;

import com.github.javafaker.Faker;
import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.yaoxing.demo.pojo.Person;

import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Aggregate extends MongoBase {
    public Aggregate(MongoClient client) {
        super(client, "demo");
    }

    public void AggregateByFavouriteColor() {
        Faker faker = new Faker();
        String color = faker.color().name();
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Document> coll = db.getCollection("Person", Document.class);

        Block<Document> printBlock = new Block<Document>() {
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };
        /*
        Aggregate by color. Equalent console script:
        db.Person.aggregate([
            {$unwind: "$favouriteColor"},
            {$group: {_id: "$favouriteColor", count: {$sum: 1}}
        ]);
        */
        coll.aggregate(Arrays.asList(
                Aggregates.unwind("$favouriteColor"),
                Aggregates.group("$favouriteColor", Accumulators.sum("count", 1))
        )).forEach(printBlock);
    }
}
