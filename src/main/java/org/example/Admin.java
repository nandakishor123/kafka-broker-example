package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Admin {

    public static void main(String [] args) {
        ApplicationContext context = SpringApplication.run(Admin.class, args);

        Producer producer = context.getBean(Producer.class);
        Consumer consumer = context.getBean(Consumer.class);

        System.out.println("Starting producer");
        producer.produce();
        System.out.println("Starting consumer");
        consumer.consume();
    }
}
