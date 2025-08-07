package com.example.speechtotext.controller;

import com.example.speechtotext.service.SpeechToTextService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SpeechToTextController {

    private final SpeechToTextService speechToTextService;

    public SpeechToTextController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    /**
     * Serve the main HTML page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Process video/audio file and return transcription
     */
    @PostMapping("/api/video-to-text")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processVideoToText(@RequestParam("video") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate file
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("error", "Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }

            // Check file size (500MB limit)
            if (file.getSize() > 500 * 1024 * 1024) {
                response.put("success", false);
                response.put("error", "File size must be less than 500MB");
                return ResponseEntity.badRequest().body(response);
            }

            // Process the file
            String transcript = speechToTextService.processVideoToText(file);
            
            response.put("success", true);
            response.put("transcript", transcript);
            response.put("filename", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("error", "Failed to process file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Process audio file and return both transcription and generated audio
     */
    @PostMapping("/api/audio-to-text")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processAudioToText(@RequestParam("audio") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate file
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("error", "Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }

            // Check file size
            if (file.getSize() > 500 * 1024 * 1024) {
                response.put("success", false);
                response.put("error", "File size must be less than 500MB");
                return ResponseEntity.badRequest().body(response);
            }

            // Process the file
            SpeechToTextService.AudioTranscriptionResult result = 
                speechToTextService.processAudioWithResponse(file);
            
            response.put("success", true);
            response.put("transcript", result.getTranscript());
            response.put("hasGeneratedAudio", result.hasGeneratedAudio());
            response.put("filename", file.getOriginalFilename());
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("success", false);
            response.put("error", "Failed to process file: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Download generated audio file
     */
    @PostMapping("/api/download-audio")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadGeneratedAudio(@RequestParam("audio") MultipartFile file) {
        try {
            SpeechToTextService.AudioTranscriptionResult result = 
                speechToTextService.processAudioWithResponse(file);
            
            if (result.hasGeneratedAudio()) {
                ByteArrayResource resource = new ByteArrayResource(result.getGeneratedAudio());
                
                return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated_audio.wav\"")
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Speech-to-Text Service");
        return ResponseEntity.ok(response);
    }
} 