package github.mackoko.potoc.twitter.producer;


import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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

	public void printTweets() {
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);

		Client twitterClient = createClient(msgQueue);
		twitterClient.connect();

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
}
