package pl.lso.kazimierz.pastoralvisitmanager.generator;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

@Service
public class GeneratorService {

    private final AddressRepository addressRepository;

    private final ApartmentRepository apartmentRepository;

    private final PastoralVisitRepository pastoralVisitRepository;

    private final PriestRepository priestRepository;

    private final SeasonRepository seasonRepository;

    private List<Priest> priests;
    private List<Season> seasons;

    @Autowired
    public GeneratorService(AddressRepository addressRepository, ApartmentRepository apartmentRepository, PastoralVisitRepository pastoralVisitRepository, PriestRepository priestRepository, SeasonRepository seasonRepository) {
        this.addressRepository = addressRepository;
        this.apartmentRepository = apartmentRepository;
        this.pastoralVisitRepository = pastoralVisitRepository;
        this.priestRepository = priestRepository;
        this.seasonRepository = seasonRepository;
    }

    private void prepare() throws ParseException {
        createPriests();
        createSeasons();
    }

    private void createPriests() {
        Priest p1 = new Priest();
        p1.setName("Wiesław Kiebuła");
        Priest p2 = new Priest();
        p2.setName("Józef Jończyk");
        Priest p3 = new Priest();
        p3.setName("Paweł Mielecki");
        Priest p4 = new Priest();
        p4.setName("Mirosław Czapla");
        Priest p5 = new Priest();
        p5.setName("Krzysztof Król");

        priestRepository.save(p1);
        priestRepository.save(p2);
        priestRepository.save(p3);
        priestRepository.save(p4);
        priestRepository.save(p5);
        priests = new ArrayList<>();
        priests.addAll(asList(p1, p2, p3, p4, p5));
    }

    private void createSeasons() throws ParseException {
        Season s1 = new Season();
        s1.setStartDate(DateUtils.parseDate("2014-12-01", "yyyy-MM-dd"));
        s1.setEndDate(DateUtils.parseDate("2015-02-01", "yyyy-MM-dd"));
        s1.setName("2014");
        Season s2 = new Season();
        s2.setStartDate(DateUtils.parseDate("2015-12-01", "yyyy-MM-dd"));
        s2.setEndDate(DateUtils.parseDate("2016-02-01", "yyyy-MM-dd"));
        s2.setName("2015");
        Season s3 = new Season();
        s3.setStartDate(DateUtils.parseDate("2016-12-01", "yyyy-MM-dd"));
        s3.setEndDate(DateUtils.parseDate("2017-02-01", "yyyy-MM-dd"));
        s3.setName("2016");
        Season s4 = new Season();
        s4.setStartDate(DateUtils.parseDate("2017-12-01", "yyyy-MM-dd"));
        s4.setEndDate(DateUtils.parseDate("2018-02-01", "yyyy-MM-dd"));
        s4.setName("2017");
        s4.setCurrent(true);
        seasonRepository.save(s1);
        seasonRepository.save(s2);
        seasonRepository.save(s3);
        seasonRepository.save(s4);
        seasons = new ArrayList<>();
        seasons.addAll(asList(s1, s2, s3, s4));
    }

    public void generate() throws URISyntaxException, IOException, ParseException {
        System.out.println("GENERATING....");

        prepare();

        Path path = Paths.get(getClass().getClassLoader()
                .getResource("PL_streets.csv").toURI());

        List<String> list = new ArrayList<>();
        Files.lines(path).forEach(line -> list.add(line.trim()));


        Set<Integer> usedNumbers = new HashSet<>();
        for (int i = 0; i < 25; i++) {
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
                address.setApartments(generateApartments(address, nextInt(1, 31)));
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
