import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'export-address',
  templateUrl: './export-address.component.html',
  styleUrls: ['./export-address.component.scss']
})
export class ExportAddressComponent implements OnInit {

  @Input() addressId: number;

  constructor() { }

  ngOnInit() {
  }

}
