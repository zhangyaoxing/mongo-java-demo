package com.mongodb.yaoxing.demo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    private static Properties properties;
    static {
        getConfig();
    }

    public static Properties getConfig() {
        if (properties == null) {
            properties = new Properties();
            try {
                InputStream configStream = Utils.class.getResourceAsStream("/config.properties");
                properties.load(configStream);
            } catch(IOException e) {
                System.out.println(e.toString());
            }
        }

        return properties;
    }
    public static MongoClient getMongoClient() {
        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString(getConfig().getProperty("CONN_STRING"));
        MongoClient client = MongoClients.create(connStr);
        return client;
    }
}
