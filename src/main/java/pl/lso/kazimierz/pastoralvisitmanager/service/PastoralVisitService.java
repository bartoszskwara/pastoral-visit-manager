package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.NewPastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PastoralVisitRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;


@Service
public class PastoralVisitService {

    private PastoralVisitRepository pastoralVisitRepository;
    private ApartmentRepository apartmentRepository;
    private PriestRepository priestRepository;

    @Autowired
    public PastoralVisitService(PastoralVisitRepository pastoralVisitRepository,
                                ApartmentRepository apartmentRepository,
                                PriestRepository priestRepository) {
        this.pastoralVisitRepository = pastoralVisitRepository;
        this.apartmentRepository = apartmentRepository;
        this.priestRepository = priestRepository;
    }

    public PastoralVisit addNewPastoralVisit(NewPastoralVisit newPastoralVisit) {
        if(newPastoralVisit == null) {
            throw new NotFoundException("Pastoral visit data not found");
        }
        Apartment apartment = apartmentRepository.findOne(newPastoralVisit.getApartmentId());
        if(apartment == null) {
            throw  new NotFoundException("Apartment not found");
        }
        Priest priest = priestRepository.findOne(newPastoralVisit.getPriestId());
        if(priest == null) {
            throw  new NotFoundException("Priest not found");
        }

        PastoralVisit pastoralVisit = new PastoralVisit();
        pastoralVisit.setDate(newPastoralVisit.getDate());
        pastoralVisit.setValue(newPastoralVisit.getValue());
        pastoralVisit.setApartment(apartment);
        pastoralVisit.setPriest(priest);
        return pastoralVisitRepository.save(pastoralVisit);
    }

    public PastoralVisit updatePastoralVisit(Long id, NewPastoralVisit newPastoralVisit) {
        if(newPastoralVisit == null) {
            throw new NotFoundException("Pastoral visit data not found");
        }
        PastoralVisit pastoralVisit = pastoralVisitRepository.findOne(id);
        if(pastoralVisit == null) {
            throw  new NotFoundException("Pastoral visit not found");
        }
        Apartment apartment = apartmentRepository.findOne(newPastoralVisit.getApartmentId());
        if(apartment == null) {
            throw  new NotFoundException("Apartment not found");
        }
        Priest priest = priestRepository.findOne(newPastoralVisit.getPriestId());
        if(priest == null) {
            throw  new NotFoundException("Priest not found");
        }

        pastoralVisit.setDate(newPastoralVisit.getDate());
        pastoralVisit.setValue(newPastoralVisit.getValue());
        pastoralVisit.setApartment(apartment);
        pastoralVisit.setPriest(priest);

        return pastoralVisitRepository.save(pastoralVisit);
    }

}
