package pl.lso.kazimierz.pastoralvisitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.SeasonMapper;
import pl.lso.kazimierz.pastoralvisitmanager.service.SeasonService;

import java.util.List;

@RestController
@RequestMapping("/season")
public class SeasonController {

    private SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity getSeasons() {
        List<Season> seasons = seasonService.getSeasons();
        return ResponseEntity.ok(SeasonMapper.map(seasons));
    }
}
