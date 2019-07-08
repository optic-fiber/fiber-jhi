import { Moment } from 'moment';
import { IInter } from 'app/shared/model/inter.model';
import { IUser } from 'app/core/user/user.model';

export interface IPlanning {
  id?: number;
  initialTech?: string;
  open?: boolean;
  dateTimeCreation?: Moment;
  lastNameTech?: string;
  firstNameTech?: string;
  inters?: IInter[];
  user?: IUser;
}

export class Planning implements IPlanning {
  constructor(
    public id?: number,
    public initialTech?: string,
    public open?: boolean,
    public dateTimeCreation?: Moment,
    public lastNameTech?: string,
    public firstNameTech?: string,
    public inters?: IInter[],
    public user?: IUser
  ) {
    this.open = this.open || false;
  }
}
