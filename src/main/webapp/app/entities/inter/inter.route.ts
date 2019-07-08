import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Inter } from 'app/shared/model/inter.model';
import { InterService } from './inter.service';
import { InterComponent } from './inter.component';
import { InterDetailComponent } from './inter-detail.component';
import { InterUpdateComponent } from './inter-update.component';
import { InterDeletePopupComponent } from './inter-delete-dialog.component';
import { IInter } from 'app/shared/model/inter.model';

@Injectable({ providedIn: 'root' })
export class InterResolve implements Resolve<IInter> {
  constructor(private service: InterService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInter> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Inter>) => response.ok),
        map((inter: HttpResponse<Inter>) => inter.body)
      );
    }
    return of(new Inter());
  }
}

export const interRoute: Routes = [
  {
    path: '',
    component: InterComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'fiberApp.inter.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: InterDetailComponent,
    resolve: {
      inter: InterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.inter.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: InterUpdateComponent,
    resolve: {
      inter: InterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.inter.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: InterUpdateComponent,
    resolve: {
      inter: InterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.inter.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const interPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: InterDeletePopupComponent,
    resolve: {
      inter: InterResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'fiberApp.inter.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
