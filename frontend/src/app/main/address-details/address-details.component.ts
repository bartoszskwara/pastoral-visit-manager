import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Address} from "./model/Address";
import {HttpHeaders} from "@angular/common/http";
import {Location} from "@angular/common/";
import {Apartment} from "./model/Apartment";
import * as moment from 'moment';
import {PastoralVisit} from "./model/PastoralVisit";
import {Season} from "./model/Season";
import {SeasonService} from "../shared/service/season/season.service";
import {AddressService} from "./service/address.service";
import {PastoralVisitService} from "./service/pastoral-visit.service";
import {EnvironmentService} from "../shared/service/environment/environment.service";
import {Moment} from "moment";
import {MatDialog} from "@angular/material";
import {PastoralVisitDialog} from "./pastoral-visit-dialog/pastoral-visit-dialog";
import {Priest} from "./model/Priest";
import {PriestService} from "../shared/service/priest/priest.service";

@Component({
  selector: 'address-details',
  templateUrl: './address-details.component.html',
  styleUrls: ['./address-details.component.scss']
})
export class AddressDetailsComponent implements OnInit {

  address: Address = null;
  loading: boolean;
  displayedColumns: string[] = ['apartment', 'edit'];
  edit: {
    active: boolean;
    season: Season;
    newPastoralVisit: {
      priestId: number;
      date: Moment;
    }
  };
  seasons: Season[] = [];
  priests: Priest[] = [];
  menu: {
    apartments: {
      open: boolean;
    }
  };
  private VISIT_COMPLETED_STATUS = [
    this.env.pastoralVisitStatus().completed,
    this.env.pastoralVisitStatus().individually
  ];

  constructor(private seasonService: SeasonService, private addressService: AddressService,
              private pastoralVisitService: PastoralVisitService, public env: EnvironmentService,
              private priestService: PriestService,
              public dialog: MatDialog,
              private route: ActivatedRoute, private router: Router, private location: Location) {
    this.loading = false;
    this.edit = {
      active: false,
      season: null,
      newPastoralVisit: {
        priestId: null,
        date: null
      }
    };
    this.menu = {
      apartments: {
        open: false
      }
    };
  }

  ngOnInit() {
    this.route.paramMap.subscribe(
      params => this.getAddress(parseInt(params.get('id')))
    );
    console.log('aa1');
    this.priestService.fetchPriests()
      .subscribe(priests => {
          this.priests = priests;
        },
        error => {
          console.log('error');
        },
        () => {
          console.log('aa2');
        });
    console.log('aa3');
    this.seasonService.fetchSeasons()
      .subscribe(
        seasons => {
          this.seasons = seasons;
          this.sortSeasonsByEndDate(this.seasons);
        },
        error => {
          console.log(error);
        },
        () => {
          this.createSeasonColumns();
          this.edit = {
            active: false,
            season: null,
            newPastoralVisit: {
              priestId: this.getCurrentLoggedPriestId(),
              date: this.getCurrentDateIfInSeasonOrFirstInSeason()
            }
          };
        }
      );

  }

  private getAddress(id: number) : void {
    this.loading = true;
    this.addressService.fetchAddress(id)
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
        });
  }

  openPastoralVisitDialog(apartment: Apartment, status: string, season: Season, startDate: Moment): void {
    const dialogRef = this.dialog.open(PastoralVisitDialog, {
      width: '250px',
      data: {
        priestId: this.edit.newPastoralVisit.priestId,
        date: this.edit.newPastoralVisit.date,
        startDate: startDate,
        availablePriests: this.priests
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      if(result == null) {
        return;
      }
      this.edit.newPastoralVisit.priestId = result.priestId;
      this.edit.newPastoralVisit.date = moment(result.date, this.env.dateFormat());

      let pastoralVisit = {
        id: null,
        apartmentId: apartment.id,
        date: this.edit.newPastoralVisit.date.hours(19).format(this.env.dateFormat()),
        value: status,
        priestId: this.edit.newPastoralVisit.priestId
      };

      this.pastoralVisitService.savePastoralVisit(pastoralVisit, this.getApplicationJsonHeaders())
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
            this.getAddress(this.address.id);
            this.menu.apartments.open = true;
          });
    });
  }

  private createSeasonColumns() {
    for(let season of this.seasons) {
      this.displayedColumns.splice(1, 0, 'season'+season.name);
    }
  }

  private selectApartment(apartment: Apartment) {
    this.router.navigate(['apartment', apartment.id]);
  }

  private getPastoralVisitStatus(apartment: Apartment, season: Season): string {
    if(season == null || apartment.pastoralVisits == null || apartment.pastoralVisits.length == 0) {
      return null;
    }
    for(let visit of apartment.pastoralVisits) {
      if(this.seasonIncludesDate(season, moment(visit.date, this.env.dateFormat()))) {
        return visit.value;
      }
    }
    return null;
  }

  private countCompletedPastoralVisitsInSeason(season: Season): number {
    let count = 0;
    for(let apartment of this.address.apartments) {
      for(let visit of apartment.pastoralVisits) {
        if(this.seasonIncludesDate(season, moment(visit.date, this.env.dateFormat())) && this.VISIT_COMPLETED_STATUS.includes(visit.value)) {
          count++;
        }
      }
    }
    return count;
  }

  private goBack(): void {
    this.location.back();
  }

  private toggleEditMode(): void {
    this.edit.active = !this.edit.active;
    if(!this.edit.active) {
      this.edit.season = null;
    }
  }

  private setSeasonToEdition(season: Season): void {
    if(this.edit.season != null && this.edit.season.id == season.id) {
      this.edit.season = null;
      this.edit.active = false;
    }
    else {
      this.edit.season = season;
      this.edit.active = true;
    }
  }

  private prepareToSavePastoralVisit(apartment: Apartment, status: string, season: Season): void {
    let pastoralVisit = new PastoralVisit();
    let visit = this.getPastoralVisitFromSeason(apartment, season);
    if(visit != null) {
      pastoralVisit = {
        id: visit.id,
        date: moment(visit.date, this.env.dateFormat()).format(this.env.dateFormat()),
        value: status,
        apartmentId: apartment.id,
        priestId: visit.priestId
      }
    }
    else {
      this.openPastoralVisitDialog(apartment, status, season, this.getCurrentDateIfInSeasonOrFirstInSeason());
      return;
    }

    this.pastoralVisitService.savePastoralVisit(pastoralVisit, this.getApplicationJsonHeaders())
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
          this.getAddress(this.address.id);
          this.menu.apartments.open = true;
        });
  }

  private getApplicationJsonHeaders(): object {
    return {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
  }

  private getPastoralVisitFromSeason(apartment: Apartment, season: Season) {
    for(let visit of apartment.pastoralVisits) {
      if(this.seasonIncludesDate(season, moment(visit.date, this.env.dateFormat()))) {
        return visit;
      }
    }
    return null;
  }

  public seasonIncludesDate(season: Season, date: Moment): boolean {
    return date.isBetween(moment(season.start, this.env.dateFormat()), moment(season.end, this.env.dateFormat()));
  }

  private getLastSeason(): Season {
    return this.sortSeasonsByEndDate(this.seasons)[0];
  }

  private sortSeasonsByEndDate(seasons: Season[]): Season[] {
    return seasons.sort((s1, s2) => {
      if(moment(s1.end, this.env.dateFormat()).isAfter(moment(s2.end, this.env.dateFormat()))) {
        return -1;
      }
      if(moment(s1.end, this.env.dateFormat()).isBefore(moment(s2.end, this.env.dateFormat()))) {
        return 1;
      }
      return 0;
    });
  }

  private getCurrentLoggedPriestId(): number {
    //TODO
    return 3;
  }

  private getCurrentDateIfInSeasonOrFirstInSeason(): Moment {
    let now = moment();
    let season = this.getLastSeason();
    if(this.seasonIncludesDate(season, now)) {
      return now;
    }
    else {
      return moment(season.start, this.env.dateFormat());
    }
  }

  private isCompleted(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    return status == this.env.pastoralVisitStatus().completed;
  }

  private isRefused(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    return status == this.env.pastoralVisitStatus().refused;
  }

  private isAbsent(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    return status == this.env.pastoralVisitStatus().absent;
  }

  private isIndividually(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    return status == this.env.pastoralVisitStatus().individually;
  }

  private isNotRequested(apartment: Apartment, season: Season): boolean {
    let status = this.getPastoralVisitStatus(apartment, season);
    return status == this.env.pastoralVisitStatus().not_requested;
  }
}
