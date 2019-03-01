package github.mackoko.potoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.mackoko.potoc.user.gatherer.UserGatherer;
import github.mackoko.potoc.user.producer.UserProducer;


@Configuration
public class AppConfig {

	@Bean
	public UserProducer userProducer(UserGatherer userGatherer) {
		return new UserProducer(userGatherer);
	}

	@Bean
	public UserGatherer userGatherer(){
		return new UserGatherer();
	}
}
