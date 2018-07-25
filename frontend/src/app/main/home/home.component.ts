import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from "../../../../../frontend-old/src/environments/environment";
import {Observable, of} from "rxjs/index";
import {SimpleAddress} from "./model/SimpleAddress";
import {catchError, tap} from "rxjs/internal/operators";
import {Page} from "../shared/model/Page";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private readonly INITIAL_SIZE: number = 20;

  private addressUrl = `${environment.server.url}` + "/address";
  addresses: SimpleAddress[];
  loading: boolean;
  page: number;
  size: number;
  displayedColumns: string[] = ['no.', 'address'];

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    this.addresses = [];
    this.loading = false;
    this.page = 0;
    this.size = 5;
  }

  ngOnInit() {
    this.getAddresses(this.page, this.INITIAL_SIZE);
    this.page = this.INITIAL_SIZE / this.size;
  }

  private getChunk(): void {
    this.page = this.page + 1;
    this.getAddresses(this.page, this.size);
  }

  private getAddresses(page: number, size: number): void {
    let params = new HttpParams()
      .append('page', page.toString())
      .append('size', size.toString());

    this.fetchAddresses({ params: params })
      .subscribe(
        response => {
          this.addresses = this.addresses.concat(response.content);
        },
        error => {
          console.log('error');
          console.log(error);
        },
        () => {
          console.log('addresses fetched');
        });
  }

  private fetchAddresses(options: object) : Observable<Page<SimpleAddress>> {
    return this.http.get<Page<SimpleAddress>>(this.addressUrl, options)
      .pipe(
        tap(addresses => console.log(addresses)),
        catchError(this.handleError<Page<SimpleAddress>>("get addresses", new Page()))
      );
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log('ERROR');
      console.error(error);

      return of(result as T);
    };
  }

  private selectAddress(address: SimpleAddress) {
    this.router.navigate(['../address', address.id], { relativeTo: this.route });
  }
}
