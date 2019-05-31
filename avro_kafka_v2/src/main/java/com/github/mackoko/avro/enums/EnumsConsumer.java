package com.github.mackoko.avro.enums;


import static com.github.mackoko.avro.util.KafkaUtil.TOPIC_ENUMS;
import static com.github.mackoko.avro.util.KafkaUtil.consumerProperties;
import static java.util.Collections.singleton;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class EnumsConsumer {

	public static void main(String[] args) {
		KafkaConsumer<String,EnumWrapper> consumer = new KafkaConsumer<>(consumerProperties());
		consumer.subscribe(singleton(TOPIC_ENUMS));

		System.out.println("Waiting for data");

		while (true) {
			ConsumerRecords<String,EnumWrapper> records = consumer.poll(Duration.ofMillis(500));
			for (ConsumerRecord<String,EnumWrapper> record : records) {
				EnumWrapper enumWrapper = record.value();
				System.out.println(enumWrapper);
			}
//			consumer.commitSync();
		}

	}
}
