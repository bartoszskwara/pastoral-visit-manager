package pl.lso.kazimierz.pastoralvisitmanager.model.comparator;

import pl.lso.kazimierz.pastoralvisitmanager.model.entity.Apartment;

import java.util.Comparator;

public class ApartmentComparator implements Comparator<Apartment> {

    @Override
    public int compare(Apartment a1, Apartment a2) {

        Integer numberPartA1 = Integer.valueOf(a1.getNumber().replaceAll("\\D", ""));
        Integer numberPartA2 = Integer.valueOf(a2.getNumber().replaceAll("\\D", ""));

        String stringPartA1 = a1.getNumber().replaceAll("\\d", "");
        String stringPartA2 = a2.getNumber().replaceAll("\\d", "");

        int result = simpleCompare(numberPartA1, numberPartA2);

        if(result > 0) {
            return 1;
        }
        if(result < 0) {
            return -1;
        }
        return  simpleCompare(stringPartA1, stringPartA2);
    }

    private <T> int simpleCompare(Comparable<T> i, T j) {
        if(i == null && j == null) {
            return 0;
        }
        if(i != null && j == null) {
            return 1;
        }
        if(i == null) {
            return -1;
        }
        return i.compareTo(j);
    }
}
