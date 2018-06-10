package com.mongodb.yaoxing.demo.domain;
import com.github.javafaker.Faker;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.yaoxing.demo.pojo.Person;

public class ArrayUpdate extends MongoBase {
    public ArrayUpdate(MongoClient client) {
        super(client, "demo");
    }

    public void updateSingleElm() {
        Faker faker = new Faker();
        String color = faker.color().name();
        String toColor;
        // maker sure toColor is different from color
        while((toColor = faker.color().name()) == color);
        System.out.println(String.format("====Updating first matched faviouriteColor from \"%s\" to \"%s\".",
                color, toColor));
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        /*
        Update first matched `color` to `toColor`. Equalent console script:
        db.Person.update({favouriteColor: color}, {$set: {'favouriteColor.$': toColor}});
        */
        UpdateResult result = coll.updateMany(eq("favouriteColor", color),
                set("favouriteColor.$", toColor));
        System.out.println(String.format("====%d documents matched, %d documents updated.",
                result.getMatchedCount(), result.getModifiedCount()));
    }

    public void updateAllMatched() {
        Faker faker = new Faker();
        String color = faker.color().name();
        String toColor;
        // maker sure toColor is different from color
        while((toColor = faker.color().name()) == color);
        System.out.println(String.format("====Updating all matched faviouriteColor from \"%s\" to \"%s\".",
                color, toColor));
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        /*
        Update all matched `color` to `toColor`. Equalent console script:
        db.Person.update({favouriteColor: color}, {$set: {'favouriteColor[]': toColor}});
        */
        UpdateResult result = coll.updateMany(eq("favouriteColor", color),
                set("favouriteColor[]", toColor));
        System.out.println(String.format("====%d documents matched, %d documents updated.",
                result.getMatchedCount(), result.getModifiedCount()));
    }
}
