package com.example.demo.controllers;


import com.example.demo.entity.Indicators;
import com.example.demo.services.IndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class IndicatorsController {

    private final IndicatorsService indicatorsService;

    @Autowired
    public IndicatorsController(IndicatorsService indicatorsService) {
        this.indicatorsService = indicatorsService;
    }

    @GetMapping("/indicators/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Indicators getIndicators(@PathVariable("id") Long id) {
        return indicatorsService.getIndicators(id);
    }
}
