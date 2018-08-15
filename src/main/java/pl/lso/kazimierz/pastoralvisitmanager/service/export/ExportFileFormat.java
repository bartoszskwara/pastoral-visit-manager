package pl.lso.kazimierz.pastoralvisitmanager.service.export;

public enum ExportFileFormat {
    CSV("csv"),
    PDF("pdf");

    private String name;

    ExportFileFormat(String name) {
        this.name = name;
    }

    public static ExportFileFormat getByName(String name) {
        for(ExportFileFormat format : values()) {
            if(format.name.equals(name)) {
                return format;
            }
        }
        return null;
    }
}
