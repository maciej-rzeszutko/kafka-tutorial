package com.github.mackoko.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mackoko.kafka.producer.user.UserProducer;

@Configuration
public class AppConfig {

	@Bean
	public UserProducer userProducer(@Value("${user.generation.count}") int userCount) {
		System.out.println("user count " + userCount);
		return new UserProducer(userCount);
	}
}
