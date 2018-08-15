package pl.lso.kazimierz.pastoralvisitmanager.service.export;

import pl.lso.kazimierz.pastoralvisitmanager.model.dto.address.SelectedAddress;

public interface FileContentProvider {
    byte[] createFileContent(SelectedAddress selectedAddress);
}
