import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

export interface NewAddress {
  streetName: {
    control: FormControl
  };
}

@Component({
  selector: 'add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {

  newAddress: NewAddress = {
    streetName: {
      control: new FormControl('', [Validators.required])
    }
  };

  constructor() { }

  ngOnInit() {
  }

  getStreetNameErrorMessage() {
    return this.newAddress.streetName.control.hasError('required') ? 'You must enter a value' : '';
  }

}
