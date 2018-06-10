package com.mongodb.yaoxing.demo.domain;
import com.mongodb.client.*;
import com.github.javafaker.Faker;
import com.mongodb.yaoxing.demo.pojo.Person;

import java.util.ArrayList;
import java.util.List;

public class Generator extends MongoBase {
    public int BATCH_SIZE = 100;
    public int TOTAL_COUNT = 10000;
    public Generator(MongoClient client) {
        super(client, "demo");
    }

    public void genData() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);
        Faker faker = new Faker();
        List<Person> data = new ArrayList<Person>();
        for(int i = 0; i < TOTAL_COUNT; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.address().fullAddress(),
                    faker.date().birthday());
            data.add(person);
            if (i % BATCH_SIZE == 0) {
                coll.insertMany(data);
                data.clear();
            }
        }
        if (data.size() > 0) {
            coll.insertMany(data);
        }
    }
}
