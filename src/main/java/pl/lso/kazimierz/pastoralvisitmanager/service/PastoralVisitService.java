package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PastoralVisitRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.util.Optional;


@Service
public class PastoralVisitService {

    private PastoralVisitRepository pastoralVisitRepository;
    private ApartmentRepository apartmentRepository;
    private PriestRepository priestRepository;
    private final SeasonRepository seasonRepository;

    @Autowired
    public PastoralVisitService(PastoralVisitRepository pastoralVisitRepository,
                                ApartmentRepository apartmentRepository,
                                PriestRepository priestRepository, SeasonRepository seasonRepository) {
        this.pastoralVisitRepository = pastoralVisitRepository;
        this.apartmentRepository = apartmentRepository;
        this.priestRepository = priestRepository;
        this.seasonRepository = seasonRepository;
    }

    public PastoralVisit savePastoralVisit(PastoralVisitDto pastoralVisitDto) {
        if(pastoralVisitDto == null) {
            throw new NotFoundException("Pastoral visit data not found");
        }
        Optional<Apartment> apartment = apartmentRepository.findById(pastoralVisitDto.getApartmentId());
        if(!apartment.isPresent()) {
            throw new NotFoundException("Apartment not found");
        }
        Optional<Priest> priest = priestRepository.findById(pastoralVisitDto.getPriestId());
        if(!priest.isPresent()) {
            throw new NotFoundException("Priest not found");
        }
        Optional<Season> season = seasonRepository.findById(pastoralVisitDto.getSeasonId());
        if(!season.isPresent()) {
            throw new NotFoundException("Season not found");
        }

        if(pastoralVisitDto.getId() != null) {
            Optional<PastoralVisit> pastoralVisit = pastoralVisitRepository.findById(pastoralVisitDto.getId());
            if(pastoralVisit.isPresent()) {
                pastoralVisit.get().setValue(mapStatus(pastoralVisitDto.getValue()));
                return pastoralVisitRepository.save(pastoralVisit.get());
            }
        }

        PastoralVisit newPastoralVisit = new PastoralVisit();
        newPastoralVisit.setDate(pastoralVisitDto.getDate());
        newPastoralVisit.setValue(mapStatus(pastoralVisitDto.getValue()));
        newPastoralVisit.setApartment(apartment.get());
        newPastoralVisit.setPriest(priest.get());
        newPastoralVisit.setSeason(season.get());
        return pastoralVisitRepository.save(newPastoralVisit);
    }

    private String mapStatus(String value) {
        PastoralVisitStatus status = PastoralVisitStatus.getByName(value);
        return status != null ? status.getStatus() : PastoralVisitStatus.not_requested.getStatus();
    }
}
