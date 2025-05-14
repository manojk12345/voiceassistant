package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoiceAssistantBackendApplication {

	public static void main(String[] args) {
		 System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/resources/voiceassistant-459423-540be66968ca.json");
		SpringApplication.run(VoiceAssistantBackendApplication.class, args);
	}

}
