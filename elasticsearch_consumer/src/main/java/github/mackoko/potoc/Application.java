package github.mackoko.potoc;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import github.mackoko.potoc.consumer.ElasticSearchConsumer;


@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private ElasticSearchConsumer elasticSearchConsumer;

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(Application.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws IOException {
		LOG.info("EXECUTING : command line runner");
		elasticSearchConsumer.consumeTweets();
	}
}
