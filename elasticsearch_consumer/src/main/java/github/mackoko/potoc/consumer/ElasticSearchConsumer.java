package github.mackoko.potoc.consumer;

import static github.mackoko.potoc.consumer.ElasticSearchConfigs.createClient;
import static github.mackoko.potoc.consumer.ElasticSearchConfigs.createConsumer;

import java.io.IOException;
import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonParser;

@Component
public class ElasticSearchConsumer {

	private static JsonParser jsonParser = new JsonParser();

	private static String extractIdFromTweet(String tweetJson) {
		// gson library
		return jsonParser.parse(tweetJson)
				.getAsJsonObject()
				.get("id_str")
				.getAsString();
	}


	public void consumeTweets() throws IOException {
		Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class.getName());
		RestHighLevelClient client = createClient();

		KafkaConsumer<String, String> consumer = createConsumer("twitter_tweets");

		while (true) {
			ConsumerRecords<String, String> records =
					consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0

			Integer recordCount = records.count();
			logger.info("Received " + recordCount + " records");

			BulkRequest bulkRequest = new BulkRequest();

			for (ConsumerRecord<String, String> record : records) {

				// 2 strategies
				// kafka generic ID
				// String id = record.topic() + "_" + record.partition() + "_" + record.offset();

				// twitter feed specific id
				try {
					String id = extractIdFromTweet(record.value());

					// where we insert data into ElasticSearch
					IndexRequest indexRequest = new IndexRequest(
							"twitter",
							"tweets",
							id // this is to make our consumer idempotent
					).source(record.value(), XContentType.JSON);

					bulkRequest.add(indexRequest); // we add to our bulk request (takes no time)
				} catch (NullPointerException e) {
					logger.warn("skipping bad data: " + record.value());
				}

			}

			if (recordCount > 0) {
				BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
				logger.info("Committing offsets...");
				consumer.commitSync();
				logger.info("Offsets have been committed");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// close the client gracefully
//		 client.close();

	}
}
