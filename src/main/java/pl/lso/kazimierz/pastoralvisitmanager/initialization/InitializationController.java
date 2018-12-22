package pl.lso.kazimierz.pastoralvisitmanager.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@RestController
@RequestMapping("/initialize")
public class InitializationController {

    @Autowired
    private InitializationService initializationService;

    @GetMapping({"", "/"})
    public ResponseEntity initialize() throws ParseException, IOException, URISyntaxException {
        initializationService.initialize();
        return ResponseEntity.ok("Initialization completed.");
    }
}
