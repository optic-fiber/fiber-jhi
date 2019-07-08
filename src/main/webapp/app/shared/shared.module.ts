import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FiberSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [FiberSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [FiberSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FiberSharedModule {
  static forRoot() {
    return {
      ngModule: FiberSharedModule
    };
  }
}
