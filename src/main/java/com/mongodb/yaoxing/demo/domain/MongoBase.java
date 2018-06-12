package com.mongodb.yaoxing.demo.domain;

import com.mongodb.client.*;

/**
 * 访问MongoDB基类，提供MongoClient供派生类使用。
 */
public abstract class MongoBase {
    protected String defaultDbName;
    protected MongoClient client;

    /**
     * 构造函数
     * @param client 访问MongoDB时使用的client。
     * @param defaultDbName 默认访问的数据库名。
     */
    public MongoBase(MongoClient client, String defaultDbName) {
        this.client = client;
        this.defaultDbName = defaultDbName;
    }

    /**
     * 获取默认数据库（由派生类在初始化时传入）
     * @return
     */
    protected MongoDatabase getDefaultDatabase() {
        MongoDatabase db = this.client.getDatabase(this.defaultDbName);
        return db;
    }
}
