package com.mongodb.yaoxing.demo.domain;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Transaction extends MongoBase {
    public Transaction(MongoClient client) {
        super(client);
    }
    public static void main(String[] args) {
        System.out.println("====This is a MongoDB demo for transactions!====");

        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString("mongodb://127.0.0.1:29017/demo");
        MongoClient client = MongoClients.create(connStr);
        Transaction trans = new Transaction(client);
        // 失败的事务，不应该进数据库
        trans.failedTransaction();
        // 成功的事务，可以在trans/trans2表中查到记录
        trans.successTransaction();
    }

    public void failedTransaction() {
        ClientSession session = this.client.startSession();
        MongoCollection<Document> collection = this.getDefaultDatabase().getCollection("trans");
        try {
            session.startTransaction();
            Document failDoc = Document.parse("{message: 'you should never see this record in MongoDB.'}");
            collection.insertOne(session, failDoc);

            // 异常
            throw new Exception();

//            session.commitTransaction();
        } catch (Exception e) {
            // 事务失败，回滚已完成操作
            session.abortTransaction();
            System.out.println("====Transaction aborted!====");
        } finally {
            session.close();
        }
    }

    public void successTransaction() {
        ClientSession session = this.client.startSession();
        MongoCollection<Document> trans = this.getDefaultDatabase().getCollection("trans");
        MongoCollection<Document> trans2 = this.getDefaultDatabase().getCollection("trans2");
        // 注意事务中不能创建新集合，因此先创建好需要使用的集合
        this.getDefaultDatabase().createCollection("trans");
        this.getDefaultDatabase().createCollection("trans2");

        try {
            session.startTransaction();
            System.out.println("====Inserting 2 documents in collection: trans/trans2.====");
            Document succDoc1 = Document.parse("{message: 'This is a successful insert on trans'}");
            Document succDoc2 = Document.parse("{message: 'This is a successful insert on trans2'}");
            trans.insertOne(session, succDoc1);
            trans2.insertOne(session, succDoc2);
            session.commitTransaction();
            System.out.println("====Transaction committed!====");
        } catch (Exception e) {
            // 事务失败，回滚已完成操作
            session.abortTransaction();
        } finally {
            session.close();
        }
    }
}
