import { Apartment } from './Apartment';
import { Priest } from './Priest';

export class PastoralVisit {
  id: number;
  value: string;
  date: Date;
  apartment: Apartment;
  priest: Priest;
}
