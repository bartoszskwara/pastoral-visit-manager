import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SimpleAddress} from "./model/SimpleAddress";
import {ActivatedRoute, Router} from "@angular/router";
import {AddressService} from "../address/service/address.service";
import {MessageComponent} from "../shared/message/message.component";

export interface Filter {
  name: string;
}

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor() {}

  ngOnInit() {
  }

}
