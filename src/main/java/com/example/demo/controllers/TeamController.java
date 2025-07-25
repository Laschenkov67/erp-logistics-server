package com.example.demo.controllers;

import com.example.demo.dto.TeamDTO;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TeamController {

    private final String TEAM_NOT_FOUND = "Such team doesn't exist!";

    private final TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping("/teams")
    @ResponseStatus(HttpStatus.OK)
    public List<TeamDTO> getAllTeams() {
        List<TeamDTO> teamDTOS = new ArrayList<>();
        teamRepository.findAll().forEach(team -> teamDTOS.add(new TeamDTO(team)));
        return teamDTOS;
    }

    @GetMapping("/teams/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeamDTO getOneTeam(@PathVariable("id") long id) {
        return new TeamDTO(teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND)));
    }
}
