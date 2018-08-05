import {FormControl} from "@angular/forms";

export class AddressFormControl {
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
