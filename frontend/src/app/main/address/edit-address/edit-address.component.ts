import {Component, OnInit} from '@angular/core';
import {Address} from "../../shared/model/Address";
import {AddressDto} from "../../shared/model/AddressDto";
import {EditAddressService} from "../service/edit-address.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'edit-address',
  templateUrl: './edit-address.component.html',
  styleUrls: ['./edit-address.component.scss']
})
export class EditAddressComponent implements OnInit {

  loading: {
    main: boolean
  };
  address: Address;

  constructor(private editAddressService: EditAddressService, private router: Router, private route: ActivatedRoute) {
    this.loading = {
      main: false
    }
  }

  ngOnInit() {
    this.route.paramMap.subscribe(
      params => this.getAddress(parseInt(params.get('id')))
    );
  }

  private getAddress(id: number) : void {
    this.loading.main = true;
    this.editAddressService.fetchAddress(id)
      .subscribe(
        address => {
          this.address = address;
        },
        error => {
          this.loading.main = false;
          console.log('error');
        },
        () => {
          this.loading.main = false;
        });
  }

  saveAddress(addressDto: AddressDto) {
    console.log('saving ->', addressDto);
    this.editAddressService.save(this.address.id, addressDto)
      .subscribe(
        address => {
          console.log(address);
          this.router.navigate(['../../', address.id], {relativeTo: this.route});
        },
        error => {
          console.log(error);
        },
        () => {
          console.log('address saved');
        }
      );
  }
}
