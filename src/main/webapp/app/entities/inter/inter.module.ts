import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { FiberSharedModule } from 'app/shared';
import {
  InterComponent,
  InterDetailComponent,
  InterUpdateComponent,
  InterDeletePopupComponent,
  InterDeleteDialogComponent,
  interRoute,
  interPopupRoute
} from './';

const ENTITY_STATES = [...interRoute, ...interPopupRoute];

@NgModule({
  imports: [FiberSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [InterComponent, InterDetailComponent, InterUpdateComponent, InterDeleteDialogComponent, InterDeletePopupComponent],
  entryComponents: [InterComponent, InterUpdateComponent, InterDeleteDialogComponent, InterDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FiberInterModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
