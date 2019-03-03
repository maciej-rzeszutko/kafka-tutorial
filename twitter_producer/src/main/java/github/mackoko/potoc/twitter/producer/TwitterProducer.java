package github.mackoko.potoc.twitter.producer;


import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;

public class TwitterProducer {

	private final Logger logger = LoggerFactory.getLogger(TwitterProducer.class);

	private final Authentication twitterAuthentication;
	private final List<String> terms = Lists.newArrayList("bitcoin");

	public TwitterProducer(Authentication twitterAuthentication) {
		this.twitterAuthentication = twitterAuthentication;
	}

	public void produceTweets() {
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);

		Client twitterClient = createClient(msgQueue);
		twitterClient.connect();

		KafkaProducer<String, String> kafkaProducer = createKafkaProducer();

		while (!twitterClient.isDone()) {
			String msg = null;
			try {
				msg = msgQueue.poll(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("Client interrupted", e);
				twitterClient.stop();
			}

			if (msg != null) {
				kafkaProducer.send(new ProducerRecord<>("twitter_tweets", null, msg));
				logger.info(msg);
			}
		}
		twitterClient.stop();
	}

	private Client createClient(BlockingQueue<String> msgQueue) {

		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
		hosebirdEndpoint.trackTerms(terms);

		return new ClientBuilder()
				.name("Hosebird-Client-01")
				.authentication(twitterAuthentication)
				.hosts(hosebirdHosts)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue))
				.build();
	}

	private KafkaProducer<String, String> createKafkaProducer(){
		String bootstrapServers = "127.0.0.1:9092";

		// create Producer properties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// create safe Producer
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // this is basically the three below; acks, retires, max - assures ordering on key

		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // response from all brokers (leaders + replicas)
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
		properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5"); // how many requests parallel to the same partition

		// high throughput producer (at the expense of a bit of latency and CPU usage)
		properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20"); // how long should producer wait until creating a batch
		properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); // 32 KB batch size, if the batch is full before "linger.ms" - it will be send right away

		// create the producer
		return new KafkaProducer<>(properties);
	}
}
