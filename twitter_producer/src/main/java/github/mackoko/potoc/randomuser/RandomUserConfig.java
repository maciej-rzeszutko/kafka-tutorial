package github.mackoko.potoc.randomuser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.mackoko.potoc.randomuser.gatherer.UserGatherer;
import github.mackoko.potoc.randomuser.producer.UserProducer;


@Configuration
public class RandomUserConfig {

	@Bean
	public UserProducer userProducer(UserGatherer userGatherer) {
		return new UserProducer(userGatherer);
	}

	@Bean
	public UserGatherer userGatherer(){
		return new UserGatherer();
	}
}
