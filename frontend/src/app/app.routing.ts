import { NgModule }             from '@angular/core';
import { PreloadAllModules,RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component'

const routes: Routes = [
  {path: '', redirectTo: 'app', pathMatch: 'full'},
  {path: 'app', loadChildren: './dashboard/dashboard.module#DashboardModule'},
  { path: 'login',  component: LoginComponent },
];
@NgModule({
  imports: [ RouterModule.forRoot(routes, {preloadingStrategy: PreloadAllModules}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
