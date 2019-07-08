/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { FiberTestModule } from '../../../test.module';
import { InterUpdateComponent } from 'app/entities/inter/inter-update.component';
import { InterService } from 'app/entities/inter/inter.service';
import { Inter } from 'app/shared/model/inter.model';

describe('Component Tests', () => {
  describe('Inter Management Update Component', () => {
    let comp: InterUpdateComponent;
    let fixture: ComponentFixture<InterUpdateComponent>;
    let service: InterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FiberTestModule],
        declarations: [InterUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InterUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InterService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Inter(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Inter();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
