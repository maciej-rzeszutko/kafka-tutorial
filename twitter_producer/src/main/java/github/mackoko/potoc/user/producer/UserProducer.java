package github.mackoko.potoc.user.producer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.mackoko.potoc.user.gatherer.UserGatherer;


public class UserProducer {
	private final Logger logger = LoggerFactory.getLogger(UserProducer.class.getName());

	private final UserGatherer userGatherer;

	public UserProducer(UserGatherer userGatherer) {
		this.userGatherer = userGatherer;
	}

	public void produce(int userCount) {
		logger.info("Gathering users");
		List<String> randomUsers = userGatherer.getRandomUsers(userCount);
		logger.info("Users gathered");
	}
}
