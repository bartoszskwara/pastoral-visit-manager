package pl.lso.kazimierz.pastoralvisitmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.AddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.NewAddressDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.AddressMapper;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;


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
