mvn install &&

java -Dfile.encoding=UTF-8 -classpath $(pwd)/target/classes:$HOME/.m2/repository/org/java-websocket/Java-WebSocket/1.4.1/Java-WebSocket-1.4.1.jar:$HOME/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:$HOME/.m2/repository/org/json/json/20190722/json-20190722.jar main.Main
