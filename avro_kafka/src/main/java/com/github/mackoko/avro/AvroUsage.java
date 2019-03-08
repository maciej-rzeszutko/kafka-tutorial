package com.github.mackoko.avro;


public class AvroUsage {


	public static void main(String[] args) {
		Cat cat = Cat.newBuilder()
				.setName("puss")
				.setBreed(Breed.PERSIAN)
				.build();
	}
}
