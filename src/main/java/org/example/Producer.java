package org.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Random;

@Component
public class Producer {

    @Value("${bootstrap.servers}")
    private String serverConnection;

    @Value("${odd.topic}")
    private String oddTopic;

    @Value("${even.topic}")
    private String evenTopic;

    public void produce() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConnection);
        // this is so that our key is a string "odd" or "even" and the value
        // is an integer 1, 2, 3, etc.
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                IntegerSerializer.class.getName());

        KafkaProducer<String, Integer> producer =
                new KafkaProducer<>(properties);
        Random random = new Random();

        try {
            for (int i = 0; i <= 100; i++) {
                int value = random.nextInt(1000);
                String topic;
                if (value % 2 == 0) {
                    topic = evenTopic;
                } else {
                    topic = oddTopic;
                }
                // this will be the piece written onto kafka each time.
                ProducerRecord<String, Integer> record =
                        new ProducerRecord<>(topic, value);
                // this is how you send a record to the producer.
                producer.send(record);

                System.out.println(
                        "Sending value: " + value + " to topic: " + topic);
                Thread.sleep(300);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            producer.close();
        }
    }
}
