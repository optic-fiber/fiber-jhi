import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInter } from 'app/shared/model/inter.model';

type EntityResponseType = HttpResponse<IInter>;
type EntityArrayResponseType = HttpResponse<IInter[]>;

@Injectable({ providedIn: 'root' })
export class InterService {
  public resourceUrl = SERVER_API_URL + 'api/inters';

  constructor(protected http: HttpClient) {}

  create(inter: IInter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inter);
    return this.http
      .post<IInter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(inter: IInter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inter);
    return this.http
      .put<IInter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(inter: IInter): IInter {
    const copy: IInter = Object.assign({}, inter, {
      dateTimeInter: inter.dateTimeInter != null && inter.dateTimeInter.isValid() ? inter.dateTimeInter.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTimeInter = res.body.dateTimeInter != null ? moment(res.body.dateTimeInter) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((inter: IInter) => {
        inter.dateTimeInter = inter.dateTimeInter != null ? moment(inter.dateTimeInter) : null;
      });
    }
    return res;
  }
}
