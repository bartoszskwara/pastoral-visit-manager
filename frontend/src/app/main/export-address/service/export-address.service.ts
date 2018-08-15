import { Injectable } from '@angular/core';
import {environment} from "../../../../environments/environment";
import {catchError} from "rxjs/internal/operators";
import {Observable, of} from "rxjs/index";
import {HttpClient} from "@angular/common/http";
import {SelectedAddressDto} from "../model/SelectedAddressDto";

@Injectable({
  providedIn: 'root'
})
export class ExportAddressService {

  private exportUrl = `${environment.server.url}` + "/export";

  constructor(private http: HttpClient) { }

  /*exportToCsv(): Observable<ExportResponse> {
    return this.http.get<ExportResponse>(this.exportUrl)
      .pipe(
        catchError(this.handleError<ExportResponse>("exporting to csv", new ExportResponse()))
      );
  }*/
  exportToCsv(addressId: number): Observable<Blob> {
    return this.downloadFile(`${this.exportUrl}/address/${addressId}/format/csv`, this.getResponseTypeBlobOptions());
  }

  exportBulkCsv(data: SelectedAddressDto[]) {
    console.log('export', data);
    return this.http.post(`${this.exportUrl}/address/bulk/format/csv`, data, this.getResponseTypeBlobOptions());
  }

  exportBulkPdf(data: SelectedAddressDto[]) {
    console.log('export', data);
    return this.http.post(`${this.exportUrl}/address/bulk/format/pdf`, data, this.getResponseTypeBlobOptions());
  }

  downloadFile(url: string, options: object): Observable<Blob> {
    return this.http.get<Blob>(url, options);
  }

  private getResponseTypeBlobOptions(): object {
    return {
      responseType: 'blob'
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
