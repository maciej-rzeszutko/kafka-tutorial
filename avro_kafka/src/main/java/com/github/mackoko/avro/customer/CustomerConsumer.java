package com.github.mackoko.avro.customer;


import static com.github.mackoko.avro.util.KafkaUtil.TOPIC_CUSTOMER;
import static com.github.mackoko.avro.util.KafkaUtil.consumerProperties;

import java.time.Duration;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class CustomerConsumer {

	public static void main(String[] args) {
		KafkaConsumer<String,Customer> consumer = new KafkaConsumer<>(consumerProperties());
		consumer.subscribe(Collections.singleton(TOPIC_CUSTOMER));

		System.out.println("Waiting for data");

		while (true) {
			ConsumerRecords<String,Customer> records = consumer.poll(Duration.ofMillis(500));
			for (ConsumerRecord<String,Customer> record : records) {
				Customer customer = record.value();
				System.out.println(customer);
			}
			consumer.commitSync();
		}

		//		consumer.close();
	}
}
