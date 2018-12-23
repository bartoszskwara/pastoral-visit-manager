package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import org.apache.commons.lang3.StringUtils;
import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

import java.util.Comparator;

public class ApartmentComparator {
    static Comparator<Apartment> byNumber() {
        return (o1, o2) -> {
            Integer num1;
            Integer num2;
            try {
                num1 = Integer.parseInt(o1.getNumber().replaceAll("[\\D]", ""));
                num2 = Integer.parseInt(o2.getNumber().replaceAll("[\\D]", ""));
            } catch (Exception e) {
                return StringUtils.compare(o1.getNumber(), o2.getNumber());
            }
            return num1.compareTo(num2);
        };
    }
}
