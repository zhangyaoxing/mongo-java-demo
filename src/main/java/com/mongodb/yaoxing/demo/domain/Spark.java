package com.mongodb.yaoxing.demo.domain;

import com.github.javafaker.Faker;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.spark.MongoConnector;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.config.WriteConfig;
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
                .getOrCreate();
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());
        JavaMongoRDD<Document> mgoRdd = MongoSpark.load(jsc)
                .withPipeline(Arrays.asList(Aggregates.match(eq("favouriteColor", color))));

        Dataset<Row> implicitDS = mgoRdd.toDF();
        implicitDS.createOrReplaceTempView("Person");
        Dataset<Row> byAge = spark.sql("SELECT age, COUNT(1) AS qty FROM Person GROUP BY age");

        // 重新配置输出集合到out
        // 这里也可以重新配置其他参数，比如writeConcern
        Map<String, String> writeOverrides = new HashMap<String, String>();
        writeOverrides.put("collection", "out");
        final WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);
        final ReadConfig readConfig = ReadConfig.create(jsc).withOptions(writeOverrides);
        byAge.foreachPartition(new ForeachPartitionFunction<Row>() {
            public void call(Iterator<Row> iterator) throws Exception {
            MongoConnector mc = MongoConnector.create(writeConfig.asJavaOptions());
            mc.withCollectionDo(readConfig, Document.class, new Function<MongoCollection<Document>, Object>() {
                public Object call(MongoCollection<Document> collection) throws Exception {
                    // 使用Collection进行需要的操作
                    return null;
                }
            });
            }
        });
//        Iterator<Row> it = byAge.toLocalIterator();
//        while(it.hasNext()) {
//            Row row = it.next();
//            List<String> fieldNames = Arrays.asList(row.schema().fieldNames());
//            scala.collection.immutable.List<String> columnNames = JavaConversions.asScalaBuffer(fieldNames).toList();
//
//        }

//        MongoSpark.save(byAge, writeConfig);
    }
}
