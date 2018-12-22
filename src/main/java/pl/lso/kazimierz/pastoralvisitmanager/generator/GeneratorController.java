package pl.lso.kazimierz.pastoralvisitmanager.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lso.kazimierz.pastoralvisitmanager.generator.GeneratorService;
import pl.lso.kazimierz.pastoralvisitmanager.model.generator.InitialConfigurationDto;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@RestController
@RequestMapping("/generator")
public class GeneratorController {

    private GeneratorService generatorService;

    @Autowired
    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @GetMapping("/generate")
    public ResponseEntity generate() throws ParseException, IOException, URISyntaxException {
        generatorService.generate();
        return ResponseEntity.ok().build();
    }
}
