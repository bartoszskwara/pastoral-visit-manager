import { Injectable } from '@angular/core';
import {catchError, tap} from "rxjs/internal/operators";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Address} from "../../shared/model/Address";
import {environment} from "../../../../environments/environment";
import {Observable, of} from "rxjs/index";
import {SimpleAddress} from "../../home/model/SimpleAddress";
import {Page} from "../../shared/model/Page";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private addressUrl = `${environment.server.url}/address`;

  constructor(private http: HttpClient) { }

  fetchAddresses(page: number, size: number, name: string) : Observable<Page<SimpleAddress>> {
    let params = new HttpParams()
      .append('page', page ? page.toString() : '0')
      .append('size', size ? size.toString() : '0')
      .append('name', name);
    return this.http.get<Page<SimpleAddress>>(`${this.addressUrl}/chunk`, {params: params})
      .pipe(
        catchError(this.handleError<Page<SimpleAddress>>("get addresses chunk", new Page()))
      );
  }

  fetchAllAddresses() : Observable<SimpleAddress[]> {
    return this.http.get<SimpleAddress[]>(this.addressUrl)
      .pipe(
        catchError(this.handleError<SimpleAddress[]>("get addresses", []))
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
