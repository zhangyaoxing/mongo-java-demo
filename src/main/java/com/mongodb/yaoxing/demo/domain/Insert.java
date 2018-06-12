package com.mongodb.yaoxing.demo.domain;
import com.mongodb.client.*;
import com.github.javafaker.Faker;
import com.mongodb.yaoxing.demo.pojo.Person;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类用于演示如何插入数据。
 */
public class Insert extends MongoBase {
    public int BATCH_SIZE = 100;
    public int TOTAL_COUNT = 10000;
    public int ARRAY_LEN = 5;
    public Insert(MongoClient client) {
        super(client);
    }

    /**
     * 清理工作
     */
    public void cleanup() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);

        // 删除目标表
        coll.drop();
        System.out.println(String.format("====Collection %s dropped!", "Person"));

        // 在目标表上创建索引（后续其他操作将会使用）
        coll.createIndex(eq("favouriteColor", 1));
        System.out.println(String.format("====Index created on field '%s'!", "favouriteColor"));
    }

    /**
     * 批量插入数据。这些数据将为其他演示功能使用。
     */
    public void insertData() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);
        Faker faker = new Faker();
        List<Person> data = new ArrayList<Person>();
        for(int i = 0; i < TOTAL_COUNT; i++) {
            // 每条数据中含有ARRAY_LEN数量的颜色数组
            List<String> colors = new ArrayList<String>();
            for (int j = 0; j < ARRAY_LEN; j++) {
                colors.add(faker.color().name());
            }

            // 生成POJO
            Person person = new Person(
                    faker.name().fullName(),
                    faker.address().fullAddress(),
                    faker.date().birthday(),
                    colors);
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
}
