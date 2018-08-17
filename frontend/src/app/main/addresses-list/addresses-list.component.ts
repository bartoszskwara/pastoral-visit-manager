import { Component, OnInit } from '@angular/core';
import {SimpleAddress} from "../home/model/SimpleAddress";
import {Filter} from "../home/home.component";
import {AddressService} from "../address/service/address.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'addresses-list',
  templateUrl: './addresses-list.component.html',
  styleUrls: ['./addresses-list.component.scss']
})
export class AddressesListComponent implements OnInit {

  private readonly INITIAL_SIZE: number = 20;
  addresses: SimpleAddress[] = [];
  loading: boolean;
  page: number;
  size: number;
  displayedColumns: string[] = ['no.', 'address', 'apartmentCount', 'addressEdit'];
  filter: Filter;

  constructor(private addressService: AddressService, private router: Router, private route: ActivatedRoute) {
    this.addresses = [];
    this.loading = false;
    this.page = 0;
    this.size = 5;
    this.filter = {
      name: ''
    };
  }

  ngOnInit() {
    this.getAddresses(this.page, this.INITIAL_SIZE, this.filter);
    this.page = this.INITIAL_SIZE / this.size;
  }

  private getChunk(): void {
    this.page = this.page + 1;
    this.getAddresses(this.page, this.size, this.filter);
  }

  private getAddresses(page: number, size: number, filter: Filter): void {
    this.loading = true;
    console.log('fir', this.addresses);
    this.addressService.fetchAddresses(page, size, filter.name)
      .subscribe(
        response => {
          if(response.content) {
            this.addresses = this.addresses.concat(response.content);
          }
        },
        error => {
          console.log('error');
          console.log(error);
          this.addresses = [];
          this.loading = false;
        },
        () => {
          this.loading = false;
          console.log('addresses fetched');
        });
  }

  private selectAddress(address: SimpleAddress) {
    this.router.navigate(['../address', address.id], { relativeTo: this.route });
  }

  applyFilter(): void {
    this.loading = true;
    this.addressService.fetchAddresses(0, this.INITIAL_SIZE, this.filter.name)
      .subscribe(
        response => {
          this.addresses = response.content;
        },
        error => {
          console.log('error');
          console.log(error);
          this.loading = false;
        },
        () => {
          this.loading = false;
          console.log('addresses fetched');
        });
  }

  goToAddressEdit(address: SimpleAddress): void {
    this.router.navigate(['../address', address.id, 'edit'], { relativeTo: this.route });
  }
}
