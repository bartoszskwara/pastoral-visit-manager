import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {ENTER, COMMA, SPACE} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material";

export interface NewAddress {
  streetName: {
    control: FormControl
  };
  blockNumber: {
    control: FormControl
  };
  apartments: {
    range: number;
  };
  included: {
    control: FormControl;
    chips: string[]
  };
  excluded: {
    control: FormControl;
    chips: string[]
  }
}

@Component({
  selector: 'add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {

  separatorKeyCodes = [ENTER, COMMA, SPACE]

  newAddress: NewAddress = {
    streetName: {
      control: new FormControl('', [Validators.required])
    },
    blockNumber: {
      control: new FormControl('', [Validators.required])
    },
    apartments: {
      range: 0
    },
    included: {
      control: new FormControl(''),
      chips: []
    },
    excluded: {
      control: new FormControl(''),
      chips: []
    }
  };

  constructor() { }

  ngOnInit() {
  }

  getStreetNameErrorMessage(): string {
    return AddAddressComponent.getRequiredErrorMessage(this.newAddress.streetName.control);
  }
  getBlockNumberErrorMessage(): string {
    return AddAddressComponent.getRequiredErrorMessage(this.newAddress.blockNumber.control);
  }
  private static getRequiredErrorMessage(control: FormControl): string {
    return control.hasError('required') ? 'You must enter a value' : '';
  }

  createChip(event: MatChipInputEvent, data: {control: FormControl; chips: string[]}): void {
    let value = (event.value || '').trim();
    if(value) {
      data.chips.push(value);
    }

    if (event.input) {
      event.input.value = '';
    }
  }

  removeChip(chip: string, chips: string[] ) {
    let i = chips.indexOf(chip);
    chips.splice(i, 1);
  }

  addNewAddress(newAddress: NewAddress) {
    console.log(newAddress);
  }
}
