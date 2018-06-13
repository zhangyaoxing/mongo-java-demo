# mongo-java-demo
此工程用于显示如何使用MongoDB Java Driver进行基本的MongoDB操作。

## 准备工作
此工程需要先安装Maven。请参考[Installing Apache Maven](https://maven.apache.org/install.html)查看如何安装。

## 使用
### 打包
此工程将所有代码及依赖打包为一个jar文件供测试。打包请使用：
```
mvn package
```

### 执行
```
java -jar target/mongo-demo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### 演示主题
1. 清理集合并构造10000条文档到Person集合：`Generate.java`
1. 根据数组元素值查询并更新相应的数组元素：`ArrayUpdate.java`
    1. 如何更新第一个匹配的数组元素；
    1. 如何更新全部需要的数组元素(需要MongoDB 3.6)；
1. 如何在集合上执行聚合查询：`Aggregate.java`
1. 如何查询指定条件的数组元素，要求只返回匹配的数组元素：`Find.java`
1. Spark基本操作演示：`Spark.java`