package com.example.demo.controllers;

import com.example.demo.dto.SuggestionDTO;
import com.example.demo.payload.request.SuggestionRequest;
import com.example.demo.services.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<SuggestionDTO> getAllSuggestions() {
        return suggestionService.getAllSuggestions();
    }

    @GetMapping("/suggestions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuggestionDTO getOneSuggestion(@PathVariable("id") Long id) {
        return suggestionService.getOneSuggestion(id);
    }

    @PostMapping("/suggestions")
    @ResponseStatus(HttpStatus.CREATED)
    public SuggestionDTO addOneSuggestion(@RequestBody SuggestionRequest suggestionRequest) {
        return suggestionService.addOneSuggestion(suggestionRequest);
    }

    @PutMapping("suggestions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuggestionDTO setNextPhase(@PathVariable("id") Long id) {
        return suggestionService.setNextPhase(id);
    }
}
