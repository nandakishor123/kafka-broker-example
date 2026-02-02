1 Set up kafka and zookeeper

Paste the configuration below into a file named docker-compose.yml, then run the command ```docker-compose up -d```. This will create a Kafka server, and a Zookeeper server

``` 
 services:
   zookeeper:
     image: wurstmeister/zookeeper
     container_name: zookeeper
     ports:
       - "2181:2181"
     networks:
       - kafka-net
   kafka:
     image: wurstmeister/kafka
     container_name: kafka
     ports:
       - "9092:9092"
     environment:
       KAFKA_ADVERTISED_LISTENERS: INSIDE://localhost:9092,OUTSIDE://localhost:9093
       KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
       KAFKA_LISTENERS: INSIDE://0.0.0.0:9092,OUTSIDE://0.0.0.0:9093
       KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
       KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
       KAFKA_CREATE_TOPICS: "even-numbers, odd-numbers"
     networks:
       - kafka-net
 networks:
   kafka-net:
     driver: bridge
```

2 Now we need to create two Kafka topics: odd-numbers and even-numbers, so that we can write the messages into whichever topics are suitable. To do that, run the commands below.

  ```docker-compose exec kafka kafka-topics.sh --create --topic odd-numbers --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092```
  
  ```docker-compose exec kafka kafka-topics.sh --create --topic even-numbers --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092```

3 Run Admin.java. You should see the Producer start generating random numbers and adding them to the topics based on whether they're odd or even. Once the Producer finishes running, the Consumer starts reading from the topics, and prints the value, and which topic it was read from.
