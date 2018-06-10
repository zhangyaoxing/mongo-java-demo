package com.mongodb.yaoxing.demo.domain;

import com.mongodb.client.*;

public abstract class MongoBase {
    protected String defaultDbName;
    protected MongoClient client;

    public MongoBase(MongoClient client, String defaultDbName) {
        this.client = client;
        this.defaultDbName = defaultDbName;
    }

    protected MongoDatabase getDefaultDatabase() {
        MongoDatabase db = this.client.getDatabase(this.defaultDbName);
        return db;
    }
}
