package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;

import java.util.List;


@Service
public class PriestService {

    private PriestRepository priestRepository;

    @Autowired
    public PriestService(PriestRepository priestRepository) {
        this.priestRepository = priestRepository;
    }

    public List<Priest> getPriests() {
        return this.priestRepository.findAll();
    }
}
