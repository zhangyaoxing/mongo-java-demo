# mongo-java-demo
This is a project for demonstrating how MongoDB Java driver can be used for CRUD operations.

## Prepare
This demo requires maven installed. Refer to [Installing Apache Maven](https://maven.apache.org/install.html) for installation.

## Usage
### Packing
This project packs everything together (including dependencies) in a jar file. Use maven for packing:
```
mvn package
```

### Execution
```
java -jar target/mongo-demo-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Demo topics
1. Cleanup and insert 10000 documents into Person. `Generate.java`
1. Update an array by array element value. `ArrayUpdate.java`
    1. How to update only first matched element in an array.
    1. How to update all matched elements in an array (requires MongoDB 3.6).
1. Running aggregation on an collection. `Aggregate.java`