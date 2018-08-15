package pl.lso.kazimierz.pastoralvisitmanager.service.importing;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

enum ImportFileFormat {
    CSV("csv");

    private String name;

    ImportFileFormat(String name) {
        this.name = name;
    }

    public static ImportFileFormat getByNameIgnoreCase(String name) {
        for(ImportFileFormat format : values()) {
            if(equalsIgnoreCase(format.name, name)) {
                return format;
            }
        }
        return null;
    }
}
