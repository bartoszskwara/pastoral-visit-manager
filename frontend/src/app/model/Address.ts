import { Apartment } from './Apartment';

export class Address {
  id: number;
  streetName: string;
  blockNumber: string;
  apartments: Apartment[];
}
