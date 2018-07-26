package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pl.lso.kazimierz.pastoralvisitmanager.exception.NotFoundException;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Address;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.PastoralVisit;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Priest;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Season;
import pl.lso.kazimierz.pastoralvisitmanager.repository.AddressRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.PriestRepository;
import pl.lso.kazimierz.pastoralvisitmanager.repository.SeasonRepository;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class ExportService {

    private final AddressRepository addressRepository;
    private final SeasonRepository seasonRepository;

    private static final String DELIMITER = ";";

    @Autowired
    public ExportService(AddressRepository addressRepository, SeasonRepository seasonRepository) {
        this.addressRepository = addressRepository;
        this.seasonRepository = seasonRepository;
    }

    public byte[] export(Long addressId, String format) {
        if(ExportFileFormat.getByName(format) == null) {
            throw new NotFoundException("File format is not supported");
        }

        Optional<Address> address = addressRepository.findById(addressId);
        if(!address.isPresent()) {
            throw new NotFoundException("Address not found");
        }

        List<Season> seasons = seasonRepository.findAll();
        if(isEmpty(seasons)) {
            throw new NotFoundException("No seasons");
        }
        seasons.sort(Comparator.comparing(Season::getName));
        String header = createCsvHeader(seasons);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(header);
        stringBuilder.append("\n");

        int count = 1;
        for(Apartment apartment : address.get().getApartments()) {
            stringBuilder
                    .append(count).append(DELIMITER)
                    .append(apartment.getNumber()).append(DELIMITER);
            for(Season season : seasons) {
                PastoralVisitStatus status = getPastoralVisitStatus(apartment, season);
                stringBuilder.append(status != null ? status.getStatus() : "");
                stringBuilder.append(DELIMITER);
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString().getBytes();
    }

    private String createCsvHeader(List<Season> seasons) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DELIMITER)
                .append("Apartment")
                .append(DELIMITER);
        for(Season season : seasons) {
            stringBuilder
                    .append(season.getName())
                    .append(DELIMITER);
        }
        return stringBuilder.toString();
    }

    // TODO: tabela: Status, zamiast +,-,? w bazie
    private PastoralVisitStatus getPastoralVisitStatus(Apartment apartment, Season season) {
        if(isEmpty(apartment.getPastoralVisits())) {
            return null;
        }
        for(PastoralVisit pastoralVisit : apartment.getPastoralVisits()) {
            if(seasonIncludesDate(season, pastoralVisit.getDate())) {
                return PastoralVisitStatus.getByStatus(pastoralVisit.getValue());
            }
        }
        return null;
    }

    private boolean seasonIncludesDate(Season season, Date date) {
        if(date == null) {
            return false;
        }
        return date.after(season.getStartDate()) && date.before(season.getEndDate());
    }

}
