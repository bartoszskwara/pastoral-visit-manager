import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpModule } from '@angular/http';

// Services
import {AddressListService} from './address-list/address-list.service';

// Material Design
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatCardModule} from '@angular/material/card';

// Components
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardRoutingModule } from './dashboard.routing';
import { LayoutComponent } from './layout/layout.component';
import { AddressListComponent } from './address-list/address-list.component';
import { SidebarLeftComponent } from './sidebar-left/sidebar-left.component';
import { SidebarRightComponent } from './sidebar-right/sidebar-right.component';
import { NavbarComponent } from './navbar/navbar.component';


@NgModule({
  imports: [
    CommonModule,
    DashboardRoutingModule,
    HttpModule,
    MatPaginatorModule,
    BrowserAnimationsModule,
    MatCardModule
  ],
  declarations: [
    DashboardComponent,
    LayoutComponent,
    AddressListComponent,
    SidebarLeftComponent,
    SidebarRightComponent,
    NavbarComponent
  ],
  exports: [
    DashboardComponent
  ],
  providers: [
    AddressListService
  ]
})
export class DashboardModule { }
