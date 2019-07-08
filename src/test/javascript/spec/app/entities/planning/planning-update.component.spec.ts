/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { FiberTestModule } from '../../../test.module';
import { PlanningUpdateComponent } from 'app/entities/planning/planning-update.component';
import { PlanningService } from 'app/entities/planning/planning.service';
import { Planning } from 'app/shared/model/planning.model';

describe('Component Tests', () => {
  describe('Planning Management Update Component', () => {
    let comp: PlanningUpdateComponent;
    let fixture: ComponentFixture<PlanningUpdateComponent>;
    let service: PlanningService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FiberTestModule],
        declarations: [PlanningUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlanningUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlanningUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlanningService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Planning(123);
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
        const entity = new Planning();
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
