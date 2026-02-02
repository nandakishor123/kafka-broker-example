package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Component
public class Consumer {

    @Value("${bootstrap.servers}")
    private String serverConnection;

    @Value("${odd.topic}")
    private String oddTopic;

    @Value("${even.topic}")
    private String evenTopic;

    @Value("${group.id}")
    private String groupId;

    public void consume() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConnection);
        // in producer, we used key_serializer, and here we use deserializer.
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                IntegerDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, Integer> consumer =
                new KafkaConsumer<>(properties);

        // now we let consumer subscribe to the topics we want.
        consumer.subscribe(List.of(evenTopic, oddTopic));

        try {
            while (true) {
                ConsumerRecords<String, Integer> records =
                        consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Integer> record : records) {
                    if (records.isEmpty()) {
                        break;
                    }
                    System.out.println(record.topic() + ": " + record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
