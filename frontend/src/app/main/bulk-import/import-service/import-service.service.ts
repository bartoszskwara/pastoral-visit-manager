import { Injectable } from '@angular/core';
import {SimpleAddress} from "../../home/model/SimpleAddress";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError} from "rxjs/internal/operators";
import {environment} from "../../../../environments/environment";
import {Observable, of} from "rxjs/index";
import {Page} from "../../shared/model/Page";

@Injectable({
  providedIn: 'root'
})
export class ImportService {

  private addressUrl = `${environment.server.url}/import/address`;

  constructor(private http: HttpClient) { }

  bulkImport(data: object) : Observable<String> {
    console.log(data);

    return this.http.post<String>(this.addressUrl, data)
      .pipe(
        catchError(this.handleError<String>("import addresses", ""))
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
