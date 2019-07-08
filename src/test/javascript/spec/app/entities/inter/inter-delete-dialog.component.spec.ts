/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FiberTestModule } from '../../../test.module';
import { InterDeleteDialogComponent } from 'app/entities/inter/inter-delete-dialog.component';
import { InterService } from 'app/entities/inter/inter.service';

describe('Component Tests', () => {
  describe('Inter Management Delete Component', () => {
    let comp: InterDeleteDialogComponent;
    let fixture: ComponentFixture<InterDeleteDialogComponent>;
    let service: InterService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FiberTestModule],
        declarations: [InterDeleteDialogComponent]
      })
        .overrideTemplate(InterDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
