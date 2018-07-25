import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from "./dashboard/dashboard.component";
import {TestComponent} from "./test/test.component";
import {HomeComponent} from "./home/home.component";
import {AddressDetailsComponent} from "./address-details/address-details.component";
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";

const routes: Routes = [
  { path: '',  component: DashboardComponent,
    children: [
      {path: '', redirectTo: 'home', pathMatch: 'full'},
      {path: 'home', component: HomeComponent},
      {path: 'address/:id', component: AddressDetailsComponent },
      {path: 'test', component: TestComponent},
      {path: '404', component: PageNotFoundComponent},
      {path: '**', redirectTo: '404'}
    ]
  }
];
@NgModule({
  imports: [ RouterModule.forChild(routes)],
  exports: [ RouterModule ]
})
export class MainRoutingModule {}
