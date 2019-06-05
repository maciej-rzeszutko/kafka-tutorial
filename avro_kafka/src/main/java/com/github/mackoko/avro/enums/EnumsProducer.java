package com.github.mackoko.avro.enums;


import static com.github.mackoko.avro.util.KafkaUtil.TOPIC_ENUMS;
import static com.github.mackoko.avro.util.KafkaUtil.producerProperties;
import static com.github.mackoko.avro.util.KafkaUtil.sendAndClose;

import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class EnumsProducer {
	public static void main(String[] args) {

		Random rand = new Random();
		for (int i = 0; i < 1000 ; i++) {
			sendAndClose(
					new KafkaProducer<>(producerProperties()),
					new ProducerRecord<>(
							TOPIC_ENUMS,
							Integer.toString(rand.nextInt(10)),
							EnumWrapper.newBuilder()
									.setEnumWillBeAdded(EnumWillBeAdded.ONE)
									.setEnumWillBeRemoved(EnumWillBeRemoved.ONE)
									.setEnumWillBeRenamed(EnumWillBeRenamed.ONE)
									.setEnumWillSwitchOrder(EnumWillSwitchOrder.ONE)
									.build()
					)
			);
		}

	}
}