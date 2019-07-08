import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanning } from 'app/shared/model/planning.model';

type EntityResponseType = HttpResponse<IPlanning>;
type EntityArrayResponseType = HttpResponse<IPlanning[]>;

@Injectable({ providedIn: 'root' })
export class PlanningService {
  public resourceUrl = SERVER_API_URL + 'api/plannings';

  constructor(protected http: HttpClient) {}

  create(planning: IPlanning): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planning);
    return this.http
      .post<IPlanning>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(planning: IPlanning): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planning);
    return this.http
      .put<IPlanning>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlanning>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlanning[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(planning: IPlanning): IPlanning {
    const copy: IPlanning = Object.assign({}, planning, {
      dateTimeCreation: planning.dateTimeCreation != null && planning.dateTimeCreation.isValid() ? planning.dateTimeCreation.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateTimeCreation = res.body.dateTimeCreation != null ? moment(res.body.dateTimeCreation) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((planning: IPlanning) => {
        planning.dateTimeCreation = planning.dateTimeCreation != null ? moment(planning.dateTimeCreation) : null;
      });
    }
    return res;
  }
}
