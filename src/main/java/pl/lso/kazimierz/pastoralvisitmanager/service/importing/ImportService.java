package pl.lso.kazimierz.pastoralvisitmanager.service.importing;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PastoralVisitRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.lowerCase;

@Service
public class ImportService {

    private static final String DELIMITER = ";";

    private final ApartmentRepository apartmentRepository;

    private final AddressRepository addressRepository;

    private final SeasonRepository seasonRepository;

    private final PastoralVisitRepository pastoralVisitRepository;

    private final PriestRepository priestRepository;

    @Autowired
    public ImportService(ApartmentRepository apartmentRepository, AddressRepository addressRepository, SeasonRepository seasonRepository, PastoralVisitRepository pastoralVisitRepository, PriestRepository priestRepository) {
        this.apartmentRepository = apartmentRepository;
        this.addressRepository = addressRepository;
        this.seasonRepository = seasonRepository;
        this.pastoralVisitRepository = pastoralVisitRepository;
        this.priestRepository = priestRepository;
    }


    public List<ImportResponseDto> importAddresses(ImportRequestDto importRequestDto) {
        if(importRequestDto == null) {
            throw new NotFoundException("Request not found");
        }

        List<ImportResponseDto> response = new ArrayList<>();
        try {
            validateFile(importRequestDto.getFile());
            validateFileFormat(importRequestDto.getFile().getOriginalFilename());
            importFile(importRequestDto);
            ImportResponseDto dto = createResponseDto(importRequestDto.getFile().getOriginalFilename(), null, true);
            response.add(dto);
        }
        catch(IllegalArgumentException e) {
            ImportResponseDto dto = createResponseDto(importRequestDto.getFile().getOriginalFilename(), e.getMessage(), false);
            response.add(dto);
        }

        return response;
    }

    private void importFile(ImportRequestDto request) {
        Optional<Priest> priest = priestRepository.findById(request.getPriestId());
        if(!priest.isPresent()) {
            throw new IllegalArgumentException("Priest not found");
        }
        Address address = addressRepository.findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(request.getStreetName(), request.getBlockNumber());
        if(address == null) {
            address = createAddress(request.getPrefix(), request.getStreetName(), request.getBlockNumber());
        }
        updateAddress(address, request.getFile(), priest.get());
    }

    private Address createAddress(String prefix, String streetName, String blockNumber) {
        Address address = new Address();
        address.setPrefix(prefix);
        address.setStreetName(streetName);
        address.setBlockNumber(blockNumber);
        return addressRepository.save(address);
    }

    private void updateAddress(Address address, MultipartFile file, Priest priest) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = br.readLine();
            List<Season> seasons = getSeasonsFromHeaderLine(headerLine);
            String line;
            while ((line = br.readLine()) != null) {
                String[] items = line.split(DELIMITER);
                Optional<Apartment> apartment = address.getApartments().stream().filter(a -> equalsIgnoreCase(a.getNumber(), items[0])).findFirst();
                Apartment a = apartment.orElseGet(() -> createApartment(items[0], address));
                savePastoralVisits(a, headerLine, line, seasons, priest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Season> getSeasonsFromHeaderLine(String headerLine) {
        List<Season> seasons = new ArrayList<>();
        if(headerLine != null) {
            List<String> seasonNames = parseSeasonCells(headerLine);
            seasons = seasonRepository.findAllByName(seasonNames);
        }
        return seasons;
    }

    private Apartment createApartment(String number, Address address) {
        if(number == null) {
            return null;
        }
        Apartment apartment = new Apartment();
        apartment.setAddress(address);
        apartment.setNumber(number);
        return apartmentRepository.save(apartment);
    }

    private void savePastoralVisits(Apartment apartment, String headerLine, String line, List<Season> seasons, Priest priest) {
        List<String> seasonNames = parseSeasonCells(headerLine);
        List<String> visits = parseSeasonCells(line);
        List<String> availableSeasonNames = seasons.stream().map(Season::getName).collect(Collectors.toList());

        for(int i = 0; i < visits.size(); i++) {
            if(availableSeasonNames.contains(seasonNames.get(i))) {
                PastoralVisit pastoralVisit = pastoralVisitRepository.findByApartmentIdAndSeasonId(apartment.getId(), seasons.get(i).getId());
                PastoralVisitStatus status = PastoralVisitStatus.getByStatus(visits.get(i));
                if(status != null) {
                    if(pastoralVisit == null) {
                        pastoralVisit = new PastoralVisit();
                        pastoralVisit.setApartment(apartment);
                        pastoralVisit.setSeason(seasons.get(i));
                    }
                    pastoralVisit.setValue(status.getStatus());
                    pastoralVisit.setPriest(priest);
                    pastoralVisit.setDate(null);
                    pastoralVisitRepository.save(pastoralVisit);
                }
            }
        }
    }

    private List<String> parseSeasonCells(String line) {
        String[] items = line.split(DELIMITER);
        return new ArrayList<>(Arrays.asList(items).subList(1, items.length));
    }

    private ImportResponseDto createResponseDto(String filename, String message, boolean completed) {
        ImportResponseDto dto = new ImportResponseDto();
        dto.setFileName(filename);
        dto.setErrorMessage(message);
        dto.setCompleted(completed);
        return dto;
    }

    private void validateFileFormat(String filename) {
        String[] split = filename.split("\\.");
        String format = split[split.length - 1];

        if(ImportFileFormat.getByNameIgnoreCase(lowerCase(format)) == null) {
            throw new IllegalArgumentException("File format is not supported");
        }
    }

    private void validateFile(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File not found");
        }
    }
}
