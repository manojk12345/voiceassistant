package com.example.demo;
import com.google.cloud.speech.v1.SpeechClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudConfig {
	// Bean definition for SpeechClient
    @Bean
    public SpeechClient speechClient() throws Exception {
        // Automatically uses the GOOGLE_APPLICATION_CREDENTIALS environment variable
        return SpeechClient.create();
    }

}
