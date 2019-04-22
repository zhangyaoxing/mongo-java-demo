package com.mongodb.yaoxing.demo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

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

    /**
     * 根据配置文件中的连接字符串创建MongoClient，同时加载使用POJO所需的Codec
     * @return MongoClient
     */
    public static MongoClient getMongoClientWithCodec() {
        // MongoDB连接字符串
        ConnectionString connStr = new ConnectionString(getConfig().getProperty("CONN_STRING"));
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                com.mongodb.MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        // 使用连接字符串，POJO转换器生成一个MongoDB配置
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connStr)
                .build();

        MongoClient client = MongoClients.create(settings);
        return client;
    }
}
