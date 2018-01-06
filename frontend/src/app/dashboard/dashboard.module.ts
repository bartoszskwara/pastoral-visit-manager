import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DashboardRoutingModule } from './dashboard.routing';
import { LayoutComponent } from './layout/layout.component';
import { AddressListComponent } from './address-list/address-list.component';
import { SidebarLeftComponent } from './sidebar-left/sidebar-left.component';
import { SidebarRightComponent } from './sidebar-right/sidebar-right.component';

@NgModule({
  imports: [
    CommonModule,
    DashboardRoutingModule
  ],
  declarations: [
    DashboardComponent,
    LayoutComponent,
    AddressListComponent,
    SidebarLeftComponent,
    SidebarRightComponent
  ],
  exports: [
    DashboardComponent
  ]
})
export class DashboardModule { }
