package github.mackoko.potoc.consumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchConfigs {

	public static final String HOSTNAME = "localhost";
	public static final int PORT = 9200;
	public static final String SCHEME = "http";


	public static RestHighLevelClient createClient() {
		return new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEME)));
	}

	public static KafkaConsumer<String, String> createConsumer(String topic) {

		String bootstrapServers = "127.0.0.1:9092";
		String groupId = "kafka-demo-elasticsearch";
		// create consumer configs
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // disable auto commit of offsets
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); // disable auto commit of offsets

		// create consumer
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		consumer.subscribe(Collections.singletonList(topic));

		return consumer;

	}

	private ElasticSearchConfigs() {
	}
}
