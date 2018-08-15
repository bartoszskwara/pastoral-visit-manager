import { Component, OnInit } from '@angular/core';
import {ImportService} from "./import-service/import-service.service";
import {Priest} from "../shared/model/Priest";
import {FormControl, Validators} from "@angular/forms";
import {AddressFormComponent} from "../address/address-form/address-form.component";
import {PriestService} from "../shared/service/priest/priest.service";

export class ImportRequestFormControl {
  streetName: FormControl;
  blockNumber: FormControl;
  priestId: number;
}


@Component({
  selector: 'bulk-import',
  templateUrl: './bulk-import.component.html',
  styleUrls: ['./bulk-import.component.scss']
})
export class BulkImportComponent implements OnInit {

  private importRequestFile: File = null;
  loading: boolean = false;

  importRequestFormControl: ImportRequestFormControl = {
    streetName: new FormControl('', [Validators.required]),
    blockNumber: new FormControl('', [Validators.required]),
    priestId: this.getCurrentLoggedPriestId()
  };

  priests: Priest[] = [];

  constructor(private importService: ImportService, private priestService: PriestService) { }

  ngOnInit() {
    this.getAvailablePriests();
  }

  send() {
    let data = this.prepareData();
    console.log('data', data);
    this.loading = true;
    this.importService.bulkImport(data)
      .subscribe(
        response => {
          console.log(response);
        },
        error => {
          this.loading = false;
          console.log('error');
          console.log(error);
        },
        () => {
          this.loading = false;
          console.log('imported');
          this.resetAll();
        });
  }

  private prepareData(): FormData {
    const data: FormData = new FormData();
    data.append('file', this.importRequestFile);
    data.append('streetName', this.importRequestFormControl.streetName.value);
    data.append('blockNumber', this.importRequestFormControl.blockNumber.value);
    data.append('priestId', String(this.importRequestFormControl.priestId));
    return data;
  }

  fileUploaded(file: File) {
    this.importRequestFile = file;
  }

  getAvailablePriests(): void {
    this.priestService.fetchPriests()
      .subscribe(priests => {
          this.priests = priests;
        },
        error => {
          console.log('error');
        },
        () => {}
      );
  }

  private getCurrentLoggedPriestId(): number {
    //TODO
    return 3;
  }

  getStreetNameErrorMessage(): string {
    return BulkImportComponent.getRequiredErrorMessage(this.importRequestFormControl.streetName);
  }
  getBlockNumberErrorMessage(): string {
    return BulkImportComponent.getRequiredErrorMessage(this.importRequestFormControl.blockNumber);
  }
  private static getRequiredErrorMessage(control: FormControl): string {
    return control.hasError('required') ? 'You must enter a value' : '';
  }

  private resetAll(): void {
    this.importRequestFormControl.streetName.reset();
    this.importRequestFormControl.blockNumber.reset();
    this.importRequestFormControl.priestId = this.getCurrentLoggedPriestId();
    this.importRequestFile = null;
  }
}
