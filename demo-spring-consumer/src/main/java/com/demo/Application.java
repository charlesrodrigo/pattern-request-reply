package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.Random;

@SpringBootApplication
public class Application {
	@Value("${server.name}")
	private String serverName;

	private final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@KafkaListener(id="${kafka.topic.request}", topics = "${kafka.topic.request}")
	@SendTo("${kafka.topic.reply}")
	public String listener(String in) {
		var result = in.toUpperCase() + " IN CONSUMER -> " + serverName;
		log.info(result);
		return result;
	}



}
