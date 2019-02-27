package com.github.mackoko.kafka.producer.user;

public class UserProducer {

	private final int userCount;

	public UserProducer(int userCount) {
		this.userCount = userCount;
	}


	public void produce() {
		System.out.println(this.userCount);
	}
}
