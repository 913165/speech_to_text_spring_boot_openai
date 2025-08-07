package com.example.speechtotext.service;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

@Service
public class SpeechToTextService {

    private final OpenAiService openAiService;

    public SpeechToTextService(@Value("${spring.ai.openai.api-key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }

    /**
     * Process video/audio file and extract transcription using OpenAI Whisper
     */
    public String processVideoToText(MultipartFile file) throws IOException {
        try {
            // Convert file to byte array
            byte[] audioData = file.getBytes();
            
            // Create a temporary file for the audio data
            java.io.File tempFile = java.io.File.createTempFile("audio_", "_" + file.getOriginalFilename());
            tempFile.deleteOnExit();
            
            // Write audio data to temp file
            java.nio.file.Files.write(tempFile.toPath(), audioData);
            
            // Create transcription request
            CreateTranscriptionRequest request = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .build();
            
            // Call OpenAI Whisper API for transcription
            String transcript = openAiService.createTranscription(request, tempFile).getText();
            
            // Clean up temp file
            tempFile.delete();
            
            return transcript;
            
        } catch (Exception e) {
            // Fallback to placeholder if API call fails
            return "Error transcribing audio: " + e.getMessage() + ". File: " + file.getOriginalFilename();
        }
    }

    /**
     * Process audio file and generate both text and audio response
     */
    public AudioTranscriptionResult processAudioWithResponse(MultipartFile file) throws IOException {
        // For now, we'll use a simple text-based approach
        String transcript = processVideoToText(file);
        
        // In a real implementation, you would also generate audio response
        // For now, we'll return null for generated audio
        return new AudioTranscriptionResult(transcript, null);
    }

    /**
     * Determine MIME type based on file extension
     */
    private String determineMimeType(String filename) {
        if (filename == null) {
            return "audio/mp3";
        }
        
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".mp3")) {
            return "audio/mp3";
        } else if (lowerFilename.endsWith(".wav")) {
            return "audio/wav";
        } else if (lowerFilename.endsWith(".mp4")) {
            return "video/mp4";
        } else if (lowerFilename.endsWith(".m4a")) {
            return "audio/mp4";
        } else {
            return "audio/mp3"; // Default
        }
    }

    /**
     * Result class for audio transcription with optional generated audio
     */
    public static class AudioTranscriptionResult {
        private final String transcript;
        private final byte[] generatedAudio;

        public AudioTranscriptionResult(String transcript, byte[] generatedAudio) {
            this.transcript = transcript;
            this.generatedAudio = generatedAudio;
        }

        public String getTranscript() {
            return transcript;
        }

        public byte[] getGeneratedAudio() {
            return generatedAudio;
        }

        public boolean hasGeneratedAudio() {
            return generatedAudio != null;
        }
    }
} 