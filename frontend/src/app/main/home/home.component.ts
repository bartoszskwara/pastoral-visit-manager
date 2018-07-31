import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from "rxjs/index";
import {SimpleAddress} from "./model/SimpleAddress";
import {catchError, tap} from "rxjs/internal/operators";
import {Page} from "../shared/model/Page";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {FormControl} from "@angular/forms";

export interface Filter {
  name: string;
}

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
  displayedColumns: string[] = ['no.', 'address', 'apartmentCount'];
  filter: Filter;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    this.addresses = [];
    this.loading = false;
    this.page = 0;
    this.size = 5;
    this.filter = {
      name: ''
    };
  }

  ngOnInit() {
    this.getAddresses(this.page, this.INITIAL_SIZE, this.filter);
    this.page = this.INITIAL_SIZE / this.size;
  }

  private getChunk(): void {
    this.page = this.page + 1;
    this.getAddresses(this.page, this.size, this.filter);
  }

  private getAddresses(page: number, size: number, filter: Filter): void {
    this.loading = true;
    let params = new HttpParams()
      .append('page', page.toString())
      .append('size', size.toString())
      .append('name', filter.name);

    this.fetchAddresses({ params: params })
      .subscribe(
        response => {
          this.addresses = this.addresses.concat(response.content);
        },
        error => {
          console.log('error');
          console.log(error);
          this.loading = false;
        },
        () => {
          this.loading = false;
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

  applyFilter(): void {
    console.log('filter: ' + this.filter.name);
    this.loading = true;
    let params = new HttpParams()
      .append('page', '0')
      .append('size', this.INITIAL_SIZE.toString())
      .append('name', this.filter.name);

    this.fetchAddresses({ params: params })
      .subscribe(
        response => {
          this.addresses = response.content;
        },
        error => {
          console.log('error');
          console.log(error);
          this.loading = false;
        },
        () => {
          this.loading = false;
          console.log('addresses fetched');
        });
  }
}
