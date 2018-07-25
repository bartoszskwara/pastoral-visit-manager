import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpClientModule} from "@angular/common/http";

import {MatTableModule} from "@angular/material/table";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatCardModule} from '@angular/material/card';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatButtonToggleModule} from '@angular/material/button-toggle';

import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {MomentModule} from 'ngx-moment';

import {MainRoutingModule} from "./main.routing";
import {DashboardComponent} from './dashboard/dashboard.component';
import {TestComponent} from './test/test.component';
import {HomeComponent} from './home/home.component';
import {NavbarComponent} from './navbar/navbar.component';
import {AddressDetailsComponent} from './address-details/address-details.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';

@NgModule({
  imports: [
    CommonModule,
    MainRoutingModule,
    HttpClientModule,
    InfiniteScrollModule,
    MatTableModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatCardModule,
    MatExpansionModule,
    MomentModule,
    MatButtonToggleModule
  ],
  declarations: [DashboardComponent, TestComponent, HomeComponent, NavbarComponent, AddressDetailsComponent, PageNotFoundComponent]
})
export class MainModule { }
