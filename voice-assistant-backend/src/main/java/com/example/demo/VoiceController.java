package com.example.demo;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {

    private final SpeechClient speechClient;

    @Autowired
    public VoiceController(SpeechClient speechClient) {
        this.speechClient = speechClient;
    }

    // Method to handle file upload and transcription
    @PostMapping("/upload")
    public String transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] audioBytes = file.getBytes();
        ByteString audioData = ByteString.copyFrom(audioBytes);

        // Configure the audio request for Google Speech-to-Text API
        RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder().setContent(audioData).build();
        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setLanguageCode("en-US")
                .setSampleRateHertz(16000)
                .build();

        // Recognize speech
        RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
        String transcript = response.getResultsList().get(0).getAlternativesList().get(0).getTranscript();

        // Perform tasks based on the transcribed text
        return performTask(transcript);  // Returns the result of the performed task
    }

    // Method to perform tasks based on transcribed text
    private String performTask(String transcript) {
        // Convert the transcript to lowercase for easier processing
        transcript = transcript.toLowerCase();

        if (transcript.contains("open website") || transcript.contains("open google")) {
            // Open a website task (for simplicity, we return the action as text)
            return "Opening Google website.";
        } else if (transcript.contains("what time is it")) {
            // Return the current time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return "The current time is: " + now.format(formatter);
        } else if (transcript.contains("hello")) {
            // Respond with a greeting
            return "Hello! How can I assist you today?";
        } else {
            // Default response for unrecognized commands
            return "Sorry, I didn't understand that. Can you repeat?";
        }
    }
}
