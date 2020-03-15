# Simple Calculator Server

Backend layer of Simple Calculator app

## Requisities

To run server, the following programs and platforms must be installed:

* Java SDK 8
* Apache Maven 3.6.2

## Getting Started

Before starting, maven dependencies should be installed with: 

```
mvn install
```

To start server, run:

```
java -Dfile.encoding=UTF-8 -classpath $(pwd)/target/classes:$HOME/.m2/repository/org/java-websocket/Java-WebSocket/1.4.1/Java-WebSocket-1.4.1.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:$HOME/.m2/repository/org/json/json/20190722/json-20190722.jar main.Main

```

Or simply run the following bash with:

```
./startup.sh
```

Once the server is awake, following output should be displayed:

```
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
Websocket server started on port 9000
```

Websocket server will run on port 9000

Happy hacking!