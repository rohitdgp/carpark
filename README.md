# Springboot App

Maven:
Clean + INstall
```
mvn clean install
```

This created a snapshot jar, which can be run directly using
```
java -jar <snapshot-file>.jar
```

Or The main class can be directly run using a INtellij or Similar IDE

When launched
Swagger Available at 
```
http://localhost:8080/swagger-ui/index.html
```

The code is built with an In-Memory H2 database.
Hence it mimics real work Database queries and connections.
Although its a volatile storage hence will be purged on each (re-)start.
