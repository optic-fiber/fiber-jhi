import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FiberSharedModule } from 'app/shared';
import {
  PlanningComponent,
  PlanningDetailComponent,
  PlanningUpdateComponent,
  PlanningDeletePopupComponent,
  PlanningDeleteDialogComponent,
  planningRoute,
  planningPopupRoute
} from './';

const ENTITY_STATES = [...planningRoute, ...planningPopupRoute];

@NgModule({
  imports: [FiberSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlanningComponent,
    PlanningDetailComponent,
    PlanningUpdateComponent,
    PlanningDeleteDialogComponent,
    PlanningDeletePopupComponent
  ],
  entryComponents: [PlanningComponent, PlanningUpdateComponent, PlanningDeleteDialogComponent, PlanningDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FiberPlanningModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
