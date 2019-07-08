import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Planning } from 'app/shared/model/planning.model';
import { PlanningService } from './planning.service';
import { PlanningComponent } from './planning.component';
import { PlanningDetailComponent } from './planning-detail.component';
import { PlanningUpdateComponent } from './planning-update.component';
import { PlanningDeletePopupComponent } from './planning-delete-dialog.component';
import { IPlanning } from 'app/shared/model/planning.model';

@Injectable({ providedIn: 'root' })
export class PlanningResolve implements Resolve<IPlanning> {
  constructor(private service: PlanningService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlanning> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Planning>) => response.ok),
        map((planning: HttpResponse<Planning>) => planning.body)
      );
    }
    return of(new Planning());
  }
}

export const planningRoute: Routes = [
  {
    path: '',
    component: PlanningComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'fiberApp.planning.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlanningDetailComponent,
    resolve: {
      planning: PlanningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.planning.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlanningUpdateComponent,
    resolve: {
      planning: PlanningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.planning.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlanningUpdateComponent,
    resolve: {
      planning: PlanningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.planning.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const planningPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlanningDeletePopupComponent,
    resolve: {
      planning: PlanningResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.planning.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
