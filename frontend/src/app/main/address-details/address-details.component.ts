import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Address} from "./model/Address";
import {catchError} from "rxjs/internal/operators";
import {Observable, of} from "rxjs/index";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Location} from "@angular/common/";
import {environment} from "../../../../../frontend-old/src/environments/environment";
import {Apartment} from "./model/Apartment";
import * as moment from 'moment';
import {PastoralVisit} from "./model/PastoralVisit";
import {Season} from "./model/Season";

@Component({
  selector: 'address-details',
  templateUrl: './address-details.component.html',
  styleUrls: ['./address-details.component.scss']
})
export class AddressDetailsComponent implements OnInit {

  address: Address = null;
  private loading: boolean;
  private addressUrl = `${environment.server.url}` + "/address";
  private pastoralVisitUrl = `${environment.server.url}` + "/pastoral-visit";
  private seasonUrl = `${environment.server.url}` + "/season";
  displayedColumns: string[] = ['apartment', 'edit'];
  private VISIT_COMPLETED_STATUS = ['+', 'ind.'];
  private seasons: Season[] = [];
  private LAST_SEASON: Season = null;
  private dateFormat = "YYYY-MM-DD HH:mm:ss ZZ";
  private edit: {
    active: boolean;
    season: Season;
  };
  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router, private location: Location) {
    this.loading = false;
    this.edit = {
      active: false,
      season: null
    }
  }

  ngOnInit() {
    this.route.paramMap.subscribe(
      params => this.getAddress(parseInt(params.get('id')))
    );
    this.fetchSeasons()
      .subscribe(
        seasons => {
          this.seasons = seasons;
          console.log(this.seasons);
        },
        error => {
          console.log('error when fetching seasons');
          console.log(error);
        },
        () => {
          this.LAST_SEASON = this.getLastSeason();
          for(let season of this.seasons) {
            this.displayedColumns.splice(1, 0, 'season'+season.name);
          }
        }
      );
  }

  private getAddress(id: number) : void {
    this.loading = true;
    this.fetchAddress(id)
      .subscribe(
        address => {
          this.address = address;
        },
        error => {
          this.loading = false;
          console.log('error');
        },
        () => {
          this.loading = false;
          console.log('address fetching completed');
        });
  }

  private fetchAddress(id: number): Observable<Address> {
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

  private selectApartment(apartment: Apartment) {
    this.router.navigate(['apartment', apartment.id]);
  }

  private getPastoralVisitStatus(apartment: Apartment, season: Season): string {
    if(apartment.pastoralVisits == null || apartment.pastoralVisits.length == 0) {
      return null;
    }
    for(let visit of apartment.pastoralVisits) {
      if(this.seasonIncludesDate(season, visit.date)) {
        return visit.value;
      }
    }
    return null;
  }

  private countCompletedPastoralVisits(season: Season): number {
    let count = 0;
    for(let apartment of this.address.apartments) {
      for(let visit of apartment.pastoralVisits) {
        if(this.seasonIncludesDate(season, visit.date) && this.VISIT_COMPLETED_STATUS.includes(visit.value)) {
          count++;
        }
      }
    }
    return count;
  }

  private seasonIncludesDate(season: Season, date: string): boolean {
    return moment(date, this.dateFormat).isBetween(moment(season.start, this.dateFormat), moment(season.end, this.dateFormat));
  }

  private goBack(): void {
    this.location.back();
  }

  private getNextSeasonName(): string {
    for(let season of this.seasons) {
      if(season.current) {
        return season.name;
      }
    }
    return null;
  }

  private toggleEditMode(): void {
    this.edit.active = !this.edit.active;
    if(!this.edit.active) {
      this.getAddress(this.address.id);
    }
  }

  private setSeasonToEdition(season: Season): void {
    console.log('to edition: ');
    console.log(season);
    if(this.edit.season != null && this.edit.season.id == season.id) {
      this.edit.season = null;
    }
    else {
      this.edit.season = season;
    }
  }

  private savePastoralVisit(apartment: Apartment, status: string): void {

    let pastoralVisit = new PastoralVisit();
    for(let visit of apartment.pastoralVisits) {
      if(this.seasonIncludesDate(this.edit.season, visit.date)) {
        pastoralVisit = {
          id: visit.id,
          date: moment(visit.date, this.dateFormat).format(this.dateFormat),
          value: status,
          apartmentId: apartment.id,
          priestId: visit.priestId
        }
      }
    }

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };

    this.requestSavingPastoralVisit(pastoralVisit, httpOptions)
      .subscribe(
        pastoralVisit => {
          console.log('saved');
          console.log(pastoralVisit);
        },
        error => {
          console.log(error);
          },
        () => {
          console.log('saving completed');
        })
  }

  private requestSavingPastoralVisit(data: PastoralVisit, httpOptions: object): Observable<PastoralVisit> {
    return this.http.post<PastoralVisit>(this.pastoralVisitUrl, data, httpOptions)
      .pipe(
        catchError(this.handleError<PastoralVisit>("saving pastoral visit", new PastoralVisit()))
      );
  }

  private fetchSeasons(): Observable<Season[]> {
    return this.http.get<Season[]>(this.seasonUrl)
      .pipe(
        catchError(this.handleError<Season[]>("saving pastoral visits", []))
      );
  }

  private getLastSeason(): Season {
    return this.sortSeasonsByEndDate()[0];
  }

  private sortSeasonsByEndDate(): Season[] {
    return this.seasons.sort((s1, s2) => {
      if(moment(s1.end, this.dateFormat).isAfter(moment(s2.end, this.dateFormat))) {
        return -1;
      }
      if(moment(s1.end, this.dateFormat).isBefore(moment(s2.end, this.dateFormat))) {
        return 1;
      }
      return 0;
    });
  }

  private isCompleted(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    if(status == '+') {
      return true;
    }
    return false;
  }

  private isRefused(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    if(status == '-') {
      return true;
    }
    return false;
  }

  private isAbsent(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    if(status == '?') {
      return true;
    }
    return false;
  }

  private isIndividually(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    if(status == 'ind.') {
      return true;
    }
    return false;
  }

  private isNotRequested(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    if(status == 'x') {
      return true;
    }
    return false;
  }
}
