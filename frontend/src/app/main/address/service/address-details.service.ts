import {Injectable} from '@angular/core';
import {catchError} from "rxjs/internal/operators";
import {Observable, of} from "rxjs/index";
import {Address} from "../address-details/model/Address";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AddressDetailsService {

  private addressUrl = `${environment.server.url}/address`;

  constructor(private http: HttpClient) { }

  fetchAddress(id: number): Observable<Address> {
    return this.http.get<Address>(`${this.addressUrl}/${id}`)
      .pipe(
        catchError(this.handleError<Address>("get address details", new Address()))
      );
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log('ERROR');
      console.error(error);
      return of(result as T);
    };
  }
}
