import { Component, OnInit } from '@angular/core';
import {Address} from '../../model/Address';
import {Page} from '../../model/Page';
import {AddressListService} from './address-list.service';
import {PageEvent} from '@angular/material';

@Component({
  selector: 'address-list',
  templateUrl: './address-list.component.html',
  styleUrls: ['./address-list.component.scss']
})
export class AddressListComponent implements OnInit {

  loading: boolean = false;
  addresses: Address[] = null;
  page: Page = {
    number: 0,
    totalElements: 100,
    size: 10,
    sizeOptions: [1, 5, 10, 25, 100],
    sort: {
      field: 'streetName',
      dir: 'asc'
    }
  }
  constructor(private addressListService: AddressListService) { }

  ngOnInit() {
    this.getAddresses();
  }

  private getAddresses(): void {
    this.loading = true;
    this.addressListService.getAddresses(this.page)
      .subscribe(
        response => {
          console.log(response);

          this.page.size = response.size;
          this.page.number = response.number;
          this.page.totalElements = response.totalElements;

          this.addresses = response.content;
        },
        error => {
          console.log('error address list');
          console.log(error);
        },
        () => {
          this.loading = false;
          console.log('end');
        }
      );
  }

  changePage(pageEvent: PageEvent ): void {
    this.page.number = pageEvent.pageIndex;
    this.page.size = pageEvent.pageSize;
    this.getAddresses();
  }
}
