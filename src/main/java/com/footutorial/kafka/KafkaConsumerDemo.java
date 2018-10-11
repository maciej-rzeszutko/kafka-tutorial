package com.footutorial.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerDemo {

	public static void main(String[] args) {
		Properties properties = new Properties();

		// kafka bootstrap server
		properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
		properties.setProperty("key.deserializer", StringDeserializer.class.getName());
		properties.setProperty("value.deserializer", StringDeserializer.class.getName());

		properties.setProperty("group.id", "test");

		properties.setProperty("enable.auto.commit", "true");
//		properties.setProperty("enable.auto.commit", "false"); // then auto.commit.interval.ms is ignored
		properties.setProperty("auto.commit.interval.ms", "1000");

		properties.setProperty("auto.offset.reset", "earliest");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		kafkaConsumer.subscribe(Arrays.asList("second_topic"));

		while (true) {
			ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//				consumerRecord.value();
//				consumerRecord.key();
//				consumerRecord.offset();
//				consumerRecord.partition();
//				consumerRecord.topic();
//				consumerRecord.timestamp();
				System.out.println(
						"Partition: " + consumerRecord.partition() +
						", Offset: " + consumerRecord.offset() +
						", Key: " + consumerRecord.key() +
						", Value: " + consumerRecord.value()
				);
			}
//			kafkaConsumer.commitSync(); // if enable.auto.commit ==  false
		}

	}
}
