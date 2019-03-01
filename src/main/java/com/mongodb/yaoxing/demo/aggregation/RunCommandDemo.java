package com.mongodb.yaoxing.demo.aggregation;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.yaoxing.demo.MongoBase;
import com.mongodb.yaoxing.demo.Utils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 在3.4以前使用runCommand方式执行aggregate的用户，
 * 在3.6以后必须添加cursor配置属性。可以参考以下示例使用。
 * 强烈建议直接使用Java API更方便。
 */
public class RunCommandDemo extends MongoBase {
    public RunCommandDemo(MongoClient client) {
        super(client);
    }

    public void runCommand() {
        MongoDatabase db = this.getDefaultDatabase();
        /*
        等价形式：
        db.runCommand({
          aggregate: "Person", // 集合名
          pipeline: [], // 聚合查询
          cursor: { }
        });
         */
        Document comm = new Document("aggregate", "Person")
                .append("pipeline", new ArrayList<Document>())
                .append("cursor", new Document());
        Document result = db.runCommand(comm);

        // 输出第一批数据
        Document cursor = (Document) result.get("cursor");
        ArrayList<Document> docs = (ArrayList<Document>)cursor.get("firstBatch");
        for(Iterator<Document> it = docs.iterator(); it.hasNext();) {
            System.out.println(it.next().toJson());
        }

        /*
        等价形式：
        db.runCommand({
            getMore: result.cursor.id,
            collection: "bar"  // 集合名
        });
         */
        long cursorId = cursor.getLong("id");
        comm = new Document("getMore", cursorId)
                .append("collection", "Person");
        // 输出后续数据
        while(cursorId > 0) {
            result = db.runCommand(comm);
            cursor = (Document) result.get("cursor");
            cursorId = cursor.getLong("id");
            docs = (ArrayList<Document>)cursor.get("nextBatch");
            for(Iterator<Document> it = docs.iterator(); it.hasNext();) {
                System.out.println(it.next().toJson());
            }
        }
    }

    public static void main(String[] args) {
        MongoClient client = Utils.getMongoClient();
        RunCommandDemo demo = new RunCommandDemo(client);
        demo.runCommand();
    }
}
