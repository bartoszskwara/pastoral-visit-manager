import { Injectable } from '@angular/core';
import {Address} from "../address-details/model/Address";
import {catchError} from "rxjs/internal/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AddAddressService} from "./add-address.service";
import {Observable, of} from "rxjs/index";
import {environment} from "../../../../environments/environment";
import {AddressDto} from "../../shared/model/AddressDto";

@Injectable({
  providedIn: 'root'
})
export class EditAddressService {

  private addressUrl = `${environment.server.url}/address`;

  constructor(private http: HttpClient) { }

  fetchAddress(id: number): Observable<Address> {
    console.log('id', id);
    return this.http.get<Address>(`${this.addressUrl}/${id}`)
      .pipe(
        catchError(this.handleError<Address>("get address details", new Address()))
      );
  }

  public save(addressId: number, address: AddressDto): Observable<Address> {
    return this.http.put<Address>(`${this.addressUrl}/${addressId}`, address, EditAddressService.getApplicationJsonHeaders())
      .pipe(
        catchError(this.handleError<Address>("editing address", new Address()))
      );
  }

  private static getApplicationJsonHeaders(): object {
    return {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log('ERROR');
      console.error(error);
      return of(result as T);
    };
  }
}
