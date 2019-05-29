package com.github.mackoko.avro.customer;


import static com.github.mackoko.avro.util.KafkaUtil.TOPIC_CUSTOMER;
import static com.github.mackoko.avro.util.KafkaUtil.producerProperties;
import static com.github.mackoko.avro.util.KafkaUtil.sendAndClose;

import java.util.Collections;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class CustomerProducer {
	public static void main(String[] args) {
		KafkaProducer<String,Customer> kafkaProducer = new KafkaProducer<>(producerProperties());

		Customer customer = Customer.newBuilder()
				.setFirstName("John")
				.setLastName("Boe")
				.setAge(28)
				.setHeight(180.0f)
				.setWeight(100.0f)
				.setPhoneNumber("123")
				.setEmail("foobar@foo.bar")
				.setChildren(Collections.emptyList())
				.build();

		ProducerRecord<String,Customer> producerRecord = new ProducerRecord<>(TOPIC_CUSTOMER, customer);

		sendAndClose(kafkaProducer, producerRecord);
	}
}