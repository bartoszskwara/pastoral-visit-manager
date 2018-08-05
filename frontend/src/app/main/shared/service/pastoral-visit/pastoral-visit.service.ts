import {Injectable} from '@angular/core';
import {catchError} from "rxjs/internal/operators";
import {Observable, of} from "rxjs/index";
import {PastoralVisit} from "../../model/PastoralVisit";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PastoralVisitService {

  private pastoralVisitUrl = `${environment.server.url}/pastoral-visit`;

  constructor(private http: HttpClient) { }

  savePastoralVisit(data: PastoralVisit, httpOptions: object): Observable<PastoralVisit> {
    return this.http.post<PastoralVisit>(this.pastoralVisitUrl, data, httpOptions)
      .pipe(
        catchError(this.handleError<PastoralVisit>("saving pastoral visit", new PastoralVisit()))
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
