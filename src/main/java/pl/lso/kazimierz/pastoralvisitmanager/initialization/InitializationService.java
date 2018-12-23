package pl.lso.kazimierz.pastoralvisitmanager.initialization;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.generator.GeneratorService;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentHistoryRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.ApartmentRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PastoralVisitRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.trim;

@Service
public class InitializationService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PastoralVisitRepository pastoralVisitRepository;

    @Autowired
    private PriestRepository priestRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ApartmentHistoryRepository apartmentHistoryRepository;

    @Autowired
    private GeneratorService generatorService;

    void initialize() throws ParseException, IOException, URISyntaxException {
        cleanDatabase();
        feedBasicData();
        feedDataFromFile("koleda.csv");
    }

    void fixStreetNameTemporary() {
        List<Address> addresses = addressRepository.findAllByStreetNameContainingIgnoreCase("aleja");
        List<Address> addressesAl = addressRepository.findAllByStreetNameContainingIgnoreCase("al.");
        if(addresses == null) {
            addresses = new ArrayList<>();
        }
        if(addressesAl == null) {
            addressesAl = new ArrayList<>();
        }
        CollectionUtils.addAll(addresses, addressesAl);
        addresses.forEach(a -> {
            a.setPrefix("al.");
            a.setStreetName(trim(a.getStreetName().replaceAll("[Aa]l\\.|[Aa]leja", "")));
            addressRepository.save(a);
        });
    }

    private void cleanDatabase() {
        apartmentHistoryRepository.deleteAll();
        pastoralVisitRepository.deleteAll();
        priestRepository.deleteAll();
        seasonRepository.deleteAll();
        apartmentRepository.deleteAll();
        addressRepository.deleteAll();
    }

    private void feedBasicData() throws ParseException {
        generatorService.createSeasons();
        generatorService.createPriests(asList("Unknown Priest"));
    }

    private void feedDataFromFile(String file) throws IOException, URISyntaxException {
        List<String> lines = prepareListOfLinesFromFile(file);

        lines.stream().forEach(l -> {
            String[] splitted = l.split(",");
            String streetName = splitted[0];
            String blockNumber = splitted[1];
            String apartmentNumber = splitted[2];
            PastoralVisitStatus status2014 = PastoralVisitStatus.getByStatus(splitted[3]);
            PastoralVisitStatus status2015 = PastoralVisitStatus.getByStatus(splitted[4]);
            PastoralVisitStatus status2016 = PastoralVisitStatus.getByStatus(splitted[5]);
            PastoralVisitStatus status2017 = PastoralVisitStatus.getByStatus(splitted[6]);

            Priest unknownPriest = priestRepository.findByNameIgnoreCase("Unknown Priest");

            Address address = addressRepository.findByStreetNameIgnoreCaseAndBlockNumberIgnoreCase(streetName, blockNumber);
            if(address == null) {
                address = createAddress(streetName, blockNumber);
            }

            Apartment apartment = apartmentRepository.findByAddressIdAndNumber(address.getId(), apartmentNumber);
            if(apartment == null) {
                apartment = createApartment(address, apartmentNumber);
            }

            createPastoralVisit("2014", apartment, unknownPriest, status2014);
            createPastoralVisit("2015", apartment, unknownPriest, status2015);
            createPastoralVisit("2016", apartment, unknownPriest, status2016);
            createPastoralVisit("2017", apartment, unknownPriest, status2017);
        });
    }

    private PastoralVisit createPastoralVisit(String seasonName, Apartment apartment, Priest priest, PastoralVisitStatus status) {
        Season season = seasonRepository.findByName(seasonName);
        if(season != null) {
            Date date = addDaysToDate(season.getStartDate(), 1);
            PastoralVisit pastoralVisit = new PastoralVisit();
            pastoralVisit.setApartment(apartment);
            pastoralVisit.setSeason(season);
            pastoralVisit.setDate(date);
            pastoralVisit.setPriest(priest);
            pastoralVisit.setValue(status != null ? status.getStatus() : null);
            return pastoralVisitRepository.save(pastoralVisit);
        }
        return null;
    }

    private Address createAddress(String streetName, String blockNumber) {
        Address address = new Address();
        address.setStreetName(streetName);
        address.setBlockNumber(blockNumber);
        address.setPrefix("ul.");
        return addressRepository.save(address);
    }

    private Apartment createApartment(Address address, String number) {
        Apartment apartment = new Apartment();
        apartment.setAddress(address);
        apartment.setNumber(number);
        return apartmentRepository.save(apartment);
    }

    private List<String> prepareListOfLinesFromFile(String filename) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader()
                .getResource(filename).toURI());

        List<String> list = new ArrayList<>();
        Files.lines(path).forEach(line -> list.add(line.trim().replaceAll("\"", "")));
        list.remove(0);
        return list;
    }

    private Date addDaysToDate(Date date, int days) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
        localDateTime = localDateTime.plusDays(days);
        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }
}
