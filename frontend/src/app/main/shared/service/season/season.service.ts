import { Injectable } from '@angular/core';
import {Season} from "../../../address-details/model/Season";
import {environment} from "../../../../../environments/environment";
import * as moment from 'moment';
import {catchError, tap} from "rxjs/internal/operators";
import {Observable, of} from "rxjs/index";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class SeasonService {

  private seasonUrl = `${environment.server.url}` + "/season";

  constructor(private http: HttpClient) {}

  fetchSeasons(): Observable<Season[]> {
    return this.http.get<Season[]>(this.seasonUrl)
      .pipe(
        catchError(this.handleError<Season[]>("fetching seasons", []))
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
