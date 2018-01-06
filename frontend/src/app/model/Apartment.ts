import {Address} from './Address';
import {PastoralVisit} from './PastoralVisit';
import {ApartmentHistory} from './ApartmentHistory';

export class Apartment {
  id: number;
  number: string;
  address: Address;
  apartmentHistories: ApartmentHistory[];
  pastoralVisits: PastoralVisit[];
}
