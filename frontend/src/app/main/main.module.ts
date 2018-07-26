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
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatDialogModule} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';

import {InfiniteScrollModule} from "ngx-infinite-scroll";
import {MomentModule} from 'ngx-moment';

import {MainRoutingModule} from "./main.routing";
import {DashboardComponent} from './dashboard/dashboard.component';
import {TestComponent} from './test/test.component';
import {HomeComponent} from './home/home.component';
import {NavbarComponent} from './navbar/navbar.component';
import {AddressDetailsComponent} from './address-details/address-details.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {SeasonService} from "./shared/service/season/season.service";
import {AddressService} from "./address-details/service/address.service";
import {PastoralVisitService} from "./address-details/service/pastoral-visit.service";
import {PastoralVisitDialog} from "./address-details/pastoral-visit-dialog/pastoral-visit-dialog";
import {FormsModule} from "@angular/forms";
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatMomentDateModule} from "@angular/material-moment-adapter";
import {MatSelectModule} from '@angular/material/select';
import {PriestService} from "./shared/service/priest/priest.service";
import {ExportAddressComponent} from './export-address/export-address.component';
import {ExportAddressService} from "./export-address/service/export-address.service";

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
    MatButtonToggleModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatInputModule,
    FormsModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatSelectModule
  ],
  declarations: [DashboardComponent, TestComponent, HomeComponent, NavbarComponent, AddressDetailsComponent, PageNotFoundComponent, PastoralVisitDialog, ExportAddressComponent],
  entryComponents: [PastoralVisitDialog],
  providers: [AddressService, SeasonService, PastoralVisitService, PriestService, ExportAddressService]
})
export class MainModule { }
