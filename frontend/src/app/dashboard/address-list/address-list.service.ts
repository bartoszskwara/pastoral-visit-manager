import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import "rxjs/add/observable/throw";
import { environment } from "../../../environments/environment";
import { Http } from "@angular/http";
import {Page} from '../../model/Page';

@Injectable()
export class AddressListService {

  private serverUrl = `${environment.server.url}`;

  constructor(private http: Http) { }

  getAddresses(page: Page): Observable<any> {

    let params: URLSearchParams = new URLSearchParams();
    params.append('page', page.number + '');
    params.append('size', page.size + '');
    params.append('sort', page.sort.field + ',' + page.sort.dir);

    return this.http.get(this.serverUrl + '/address?' + params.toString())
      .map(res => res.json())
    }
}
