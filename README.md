# mongo-java-demo
此工程用于显示如何使用MongoDB Java Driver进行基本的MongoDB操作。

## 准备工作
此工程需要先安装Maven。请参考[Installing Apache Maven](https://maven.apache.org/install.html)查看如何安装。

## 使用

### 执行
每个类都具有`Main`方法，执行这个方法即可执行当前演示内容。

### 演示主题
1. 清理集合并构造10000条文档到Person集合：`InsertDemo.java`
1. 更新操作演示：`UpdateDemo.java`
    1. 如何更新第一个匹配的数组元素；
    1. (未完成)如何更新全部需要的数组元素(需要MongoDB 3.6)；
    1. 如何执行批量更新；
    1. 如何替换文档；
1. 如何在集合上执行聚合查询：`Aggregate.java`
1. 如何查询指定条件的数组元素，要求只返回匹配的数组元素：`FindDemo.java`
    1. 如何使用数组元素；
1. 如何按指定条件批量删除数据：`DeleteDemo.java`
1. (未完成)Spark基本操作演示：`Spark.java`
