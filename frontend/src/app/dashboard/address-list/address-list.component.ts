import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'address-list',
  templateUrl: './address-list.component.html',
  styleUrls: ['./address-list.component.scss']
})
export class AddressListComponent implements OnInit {

  loading: boolean = false;
  addresses: Address[] = null;

  constructor() { }

  ngOnInit() {
  }

}