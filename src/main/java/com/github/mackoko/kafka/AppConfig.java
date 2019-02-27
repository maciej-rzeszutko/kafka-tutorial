package com.github.mackoko.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mackoko.kafka.user.gatherer.UserGatherer;
import com.github.mackoko.kafka.user.producer.UserProducer;

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
