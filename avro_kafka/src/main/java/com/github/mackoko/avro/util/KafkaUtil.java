package com.github.mackoko.avro.util;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;

public class KafkaUtil {

	public static final String TOPIC_CUSTOMER = "customer-avro";
	public static final String TOPIC_ENUMS = "enums-avro";

	private KafkaUtil() {

	}
//     "[value.subject.name.strategy]": io.confluent.kafka.serializers.subject.RecordNameStrategy
	public static Properties producerProperties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "1");
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, "10");

		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		properties.setProperty("value.subject.name.strategy", TopicRecordNameStrategy.class.getName());

		properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

		return properties;
	}

	public static Properties consumerProperties() {
		String bootstrapServers = "127.0.0.1:9092";
		String groupId = "my-avro-consumer";
		// create consumer configs
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // disable auto commit of offsets
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");

		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
		properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
		properties.setProperty("specific.avro.reader", "true");

		return properties;
	}

	public static <T> void sendAndClose(
			KafkaProducer<String,T> kafkaProducer,
			ProducerRecord<String,T> producerRecord) {
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