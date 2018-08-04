import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {ENTER, COMMA, SPACE} from "@angular/cdk/keycodes";
import {MatChipInputEvent} from "@angular/material";
import {AddAddressService} from "../service/add-address.service";
import {NewAddress} from "./model/NewAddress";
import {ActivatedRoute, Router} from "@angular/router";
import {AddressDto} from "../../shared/model/AddressDto";

@Component({
  selector: 'add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {

  separatorKeyCodes = [ENTER, COMMA, SPACE];

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

  constructor(private addAddressService: AddAddressService, private router: Router, private route: ActivatedRoute) { }

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
    if(newAddress.streetName.control.invalid || newAddress.blockNumber.control.invalid
      || newAddress.included.control.invalid || newAddress.excluded.control.invalid) {
      return;
    }

    let newAddressDto = this.mapNewAddressDto(newAddress);

    console.log('newAddressDto ->', newAddressDto);
    this.addAddressService.save(newAddressDto)
      .subscribe(
        address => {
          console.log(address);
          this.router.navigate(['../', address.id], {relativeTo: this.route});
        },
        error => {
          console.log(error);
        },
        () => {
          console.log('address added');
        }
      );

  }

  private mapNewAddressDto(newAddress: NewAddress): AddressDto {
    let apartments = Array.from(Array(newAddress.apartments.range).keys()).map(i => (i+1).toString());
    for(let included of newAddress.included.chips) {
      apartments.push(included);
    }
    for(let excluded of newAddress.excluded.chips) {
      apartments.splice(apartments.indexOf(excluded), 1);
    }
    return {
      streetName: newAddress.streetName.control.value,
      blockNumber: newAddress.blockNumber.control.value,
      apartments: apartments
    }
  }

}
