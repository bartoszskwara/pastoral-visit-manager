import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../../environments/environment";
import {Observable, of} from "rxjs/index";
import {Address} from "../address-details/model/Address";
import {catchError} from "rxjs/internal/operators";
import {AddressDto} from "../../shared/model/AddressDto";

@Injectable({
  providedIn: 'root'
})
export class AddAddressService {

  private addressUrl = `${environment.server.url}/address`;

  constructor(private http: HttpClient) { }

  public save(address: AddressDto): Observable<Address> {
    return this.http.post<Address>(this.addressUrl, address, AddAddressService.getApplicationJsonHeaders())
      .pipe(
        catchError(this.handleError<Address>("saving new address", new Address()))
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
