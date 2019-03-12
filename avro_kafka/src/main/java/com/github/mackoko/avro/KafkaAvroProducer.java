package com.github.mackoko.avro;


import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class KafkaAvroProducer {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, "10");

		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

		KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<>(properties);
		String topic = "customer-avro";

		Customer customer = Customer.newBuilder()
				.setFirstName("John")
				.setLastName("Boe")
				.setAge(28)
				.setHeight(180.0f)
				.setWeight(100.0f)
				.setPhoneNumber("123")
				.setEmail("foobar@foo.bar")
				.build();

		ProducerRecord<String, Customer> producerRecord = new ProducerRecord<>(topic, customer);

		kafkaProducer.send(producerRecord, (RecordMetadata metadata, Exception exception) -> {
			if (exception == null) {
				System.out.println("Sucess!");
				System.out.println(metadata.toString());
			} else {
				exception.printStackTrace();
			}
		});

		kafkaProducer.flush();
		kafkaProducer.close();
	}
}