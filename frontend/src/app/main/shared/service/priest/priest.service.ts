import {Injectable} from '@angular/core';
import {catchError} from "rxjs/internal/operators";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../../../environments/environment";
import {Priest} from "../../../address-details/model/Priest";
import {Observable, of} from "rxjs/index";

@Injectable({
  providedIn: 'root'
})
export class PriestService {

  private priestUrl = `${environment.server.url}` + "/priest";

  constructor(private http: HttpClient) {}

  fetchPriests(): Observable<Priest[]> {
    return this.http.get<Priest[]>(this.priestUrl)
      .pipe(
        catchError(this.handleError<Priest[]>("fetching priests", []))
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
