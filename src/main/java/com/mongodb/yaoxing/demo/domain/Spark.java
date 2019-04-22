package com.mongodb.yaoxing.demo.domain;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.spark.MongoConnector;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.config.WriteConfig;
import com.mongodb.yaoxing.demo.pojo.Person;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;
import scala.collection.mutable.ArraySeq;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * Spark示例
 */
public class Spark {
    public Spark() {
    }

    /**
     * 按照生日分组统计
     */
    public void groupByBirthday() {
        // 随机生成要查询的颜色
        Faker faker = new Faker();
        String color = faker.color().name();

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("MongoSparkConnectorIntro")
                .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/demo.Person")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/demo.Output")
//                .config("spark.mongodb.input.partitionerOptions.partitionKey", "age")
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        ReadConfig rc = ReadConfig.create(jsc)
//                .withOption("partitioner", "MongoSplitVectorPartitioner")
                .withOption("partitionerOptions.partitionKey", "age");
        JavaMongoRDD<Document> mgoRdd = MongoSpark.load(jsc, rc)
                .withPipeline(Arrays.asList(Aggregates.match(eq("favouriteColor", color))));

        Dataset<Person> personDS = mgoRdd.toDS(Person.class);
//        personDS.createOrReplaceTempView("Person");
//        Dataset<Row> byAge = spark.sql("SELECT age, COUNT(1) AS qty FROM Person GROUP BY age");

        // 重新配置输出集合到"out"
        // 这里也可以重新配置其他参数，比如writeConcern
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", "out");
        final WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);
//        final ReadConfig readConfig = ReadConfig.create(jsc).withOptions(writeOverrides);
//        personDS.foreachPartition(new ForeachPartitionFunction<Person>() {
//            public void call(final Iterator<Person> iterator) throws Exception {
//                MongoConnector mc = MongoConnector.create(writeConfig.asJavaOptions());
//                mc.withCollectionDo(readConfig, Person.class, new Function<MongoCollection<Person>, Object>() {
//                    public Object call(MongoCollection<Person> collection) throws Exception {
//                        // 根据条件{key: value}查找记录，并更新为
//                        while (iterator.hasNext()) {
//                            Person p = iterator.next();
//                            collection.replaceOne(eq("key", "value"), p);
//                        }
//                        return null;
//                    }
//                });
//            }
//        });

        MongoSpark.save(personDS, writeConfig);
    }
}
