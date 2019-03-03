package com.mongodb.yaoxing.demo.insert;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.pojo.Person;
import com.mongodb.yaoxing.demo.pojo.Phone;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertPOJODemo extends MongoBase {
    public InsertPOJODemo(MongoClient client) {
        super(client);
    }

    /**
     * 批量插入数据。这些数据将为其他演示功能使用。
     */
    public void insertData() {
        MongoDatabase db = this.getDefaultDatabase();
        MongoCollection<Person> coll = db.getCollection("Person", Person.class);
        Faker faker = new Faker();
        List<Person> data = new ArrayList<Person>();
        for(int i = 0; i < InsertDemo.TOTAL_COUNT; i++) {
            // 每条数据中含有ARRAY_LEN数量的颜色数组
            List<String> colors = new ArrayList<String>();
            for (int j = 0; j < InsertDemo.ARRAY_LEN; j++) {
                colors.add(faker.color().name());
            }

            // 生成POJO
            Date bDay = faker.date().birthday();
            int age = (int) ((new Date().getTime() - bDay.getTime()) / 3600 / 24 / 365 / 1000);

            List<Phone> phones = new ArrayList<Phone>();
            for (int j = 0; j < InsertDemo.PHONE_TYPE.length; j++) {
                Phone phone = new Phone(InsertDemo.PHONE_TYPE[j], faker.phoneNumber().phoneNumber());
                phones.add(phone);
            }

            Person person = new Person(
                    faker.name().fullName(),
                    faker.address().fullAddress(),
                    new java.sql.Date(bDay.getTime()),
                    colors,
                    age,
                    new BigDecimal(faker.number().numberBetween(10, 100)),
                    phones);
            data.add(person);
            // 使用批量方式插入以提高效率
            if (i % InsertDemo.BATCH_SIZE == 0) {
                coll.insertMany(data);
                data.clear();
            }
        }
        if (data.size() > 0) {
            coll.insertMany(data);
        }
        System.out.println(String.format("====%d documents generated!", InsertDemo.TOTAL_COUNT));
    }
}
