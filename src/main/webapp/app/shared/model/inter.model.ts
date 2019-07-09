import { Moment } from 'moment';
import { IPlanning } from 'app/shared/model/planning.model';

export const enum TypeInterEnum {
  BAAP = 'BAAP',
  BAOC = 'BAOC',
  BAFA = 'BAFA',
  BAST = 'BAST',
  PLP = 'PLP',
  SAV = 'SAV'
}

export const enum ContractEnum {
  LM = 'LM',
  IQ = 'IQ',
  CABLE_ROUTING = 'CABLE_ROUTING'
}

export interface IInter {
  id?: number;
  nd?: string;
  typeInter?: TypeInterEnum;
  contract?: ContractEnum;
  lastNameClient?: string;
  firstNameClient?: string;
  dateTimeInter?: Moment;
  complex?: boolean;
  planning?: IPlanning;
}

export class Inter implements IInter {
  constructor(
    public id?: number,
    public nd?: string,
    public typeInter?: TypeInterEnum,
    public contract?: ContractEnum,
    public lastNameClient?: string,
    public firstNameClient?: string,
    public dateTimeInter?: Moment,
    public complex?: boolean,
    public planning?: IPlanning
  ) {
    this.complex = this.complex || false;
  }
}
