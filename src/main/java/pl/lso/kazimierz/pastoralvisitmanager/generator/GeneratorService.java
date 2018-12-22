package pl.lso.kazimierz.pastoralvisitmanager.generator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.model.generator.InitialConfigurationDto;
import pl.lso.kazimierz.pastoralvisitmanager.model.mapper.SeasonMapper;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.StringUtils.trim;

@Service
public class GeneratorService {

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

    private List<Priest> priests;
    private List<Season> seasons;

    private void prepare() throws ParseException {
        createPriests(asList("Adam", "Jozef"));
        createSeasons();
    }

    public void createPriests(List<String> names) {
        if(CollectionUtils.isEmpty(names)) {
            return;
        }
        priests = new ArrayList<>();
        for(String name : names) {
            Priest p = new Priest();
            p.setName(name);
            priests.add(p);
            priestRepository.save(p);
        }
    }

    public void createSeasons() throws ParseException {
        Season s1 = Season.builder()
                .startDate(DateUtils.parseDate("2014-12-01", "yyyy-MM-dd"))
                .endDate(DateUtils.parseDate("2015-02-01", "yyyy-MM-dd"))
                .name("2014")
                .build();
        Season s2 = Season.builder()
                .startDate(DateUtils.parseDate("2015-12-01", "yyyy-MM-dd"))
                .endDate(DateUtils.parseDate("2016-02-01", "yyyy-MM-dd"))
                .name("2015")
                .build();
        Season s3 = Season.builder()
                .startDate(DateUtils.parseDate("2016-12-01", "yyyy-MM-dd"))
                .endDate(DateUtils.parseDate("2017-02-01", "yyyy-MM-dd"))
                .name("2016")
                .build();
        Season s4 = Season.builder()
                .startDate(DateUtils.parseDate("2017-12-01", "yyyy-MM-dd"))
                .endDate(DateUtils.parseDate("2018-02-01", "yyyy-MM-dd"))
                .name("2017")
                .build();
        Season s5 = Season.builder()
                .startDate(DateUtils.parseDate("2018-12-01", "yyyy-MM-dd"))
                .endDate(DateUtils.parseDate("2019-02-01", "yyyy-MM-dd"))
                .name("2018")
                .current(true)
                .build();
        saveSeasons(asList(s1, s2, s3, s4, s5));
    }

    private void saveSeasons(List<Season> seasonsToSave) {
        if(CollectionUtils.isEmpty(seasonsToSave)) {
            return;
        }
        for(Season s : seasonsToSave) {
            seasonRepository.save(s);
        }
        seasons = new ArrayList<>();
        seasons.addAll(seasonsToSave);
    }

    void generate() throws URISyntaxException, IOException, ParseException {
        System.out.println("GENERATING....");

        prepare();

        Path path = Paths.get(getClass().getClassLoader()
                .getResource("PL_streets.csv").toURI());

        List<String> list = new ArrayList<>();
        Files.lines(path).forEach(line -> list.add(line.trim()));


        Set<Integer> usedNumbers = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            System.out.println("\n>>>>>>>> " + i + "\n");
            Integer randomNumber;
            do {
                randomNumber = nextInt(0, list.size());
            }
            while(usedNumbers.contains(randomNumber));
            usedNumbers.add(randomNumber);

            String line = list.get(randomNumber);
            String[] items = line.split(";");

            String streetName = trim(items[8].concat(" ").concat(items[7]));
            String prefix = trim(items[6]);

            Set<Integer> usedBlocks = new HashSet<>();
            for(int j = 0; j < nextInt(1, 5); j++) {
                Integer randomBlockNumber;
                do {
                    randomBlockNumber = nextInt(1, 100);
                }
                while(usedBlocks.contains(randomBlockNumber));
                usedBlocks.add(randomBlockNumber);

                Address address = new Address();
                address.setPrefix(prefix);
                address.setStreetName(streetName);
                address.setBlockNumber(String.valueOf(randomBlockNumber));
                addressRepository.save(address);
                address.setApartments(generateApartments(address, nextInt(1, 4)));
            }

        }
    }

    private List<Apartment> generateApartments(Address address, Integer count) throws ParseException {
        List<Apartment> apartments = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            Apartment apartment = new Apartment();
            apartment.setNumber(String.valueOf(i+1));
            apartment.setAddress(address);
            apartmentRepository.save(apartment);
            apartment.setPastoralVisits(generatePastoralVisits(apartment));
            apartments.add(apartment);
        }
        return apartments;
    }

    private List<PastoralVisit> generatePastoralVisits(Apartment apartment) throws ParseException {
        List<PastoralVisit> pastoralVisits = new ArrayList<>();

        for (Season season : seasons) {
            PastoralVisit pastoralVisit = new PastoralVisit();
            pastoralVisit.setDate(season.getStartDate());
            pastoralVisit.setValue(PastoralVisitStatus.values()[nextInt(0, PastoralVisitStatus.values().length)].getStatus());
            pastoralVisit.setPriest(priests.get(nextInt(0, priests.size())));
            pastoralVisit.setApartment(apartment);
            pastoralVisit.setSeason(season);
            pastoralVisitRepository.save(pastoralVisit);
        }
        return pastoralVisits;
    }

}
