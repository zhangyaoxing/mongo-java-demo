package com.mongodb.yaoxing.demo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工具类
 */
public class Utils {
    private static Properties properties;
    static {
        getConfig();
    }

    /**
     * 从配置文件中加载配置
     * @return 配置
     */
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

    /**
     * 根据配置文件中的连接字符串创建MongoClient
     * @return MongoClient
     */
    public static MongoClient getMongoClient() {
        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString(getConfig().getProperty("CONN_STRING"));
        MongoClient client = MongoClients.create(connStr);
        return client;
    }
}
