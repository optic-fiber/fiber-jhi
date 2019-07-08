/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FiberTestModule } from '../../../test.module';
import { InterDetailComponent } from 'app/entities/inter/inter-detail.component';
import { Inter } from 'app/shared/model/inter.model';

describe('Component Tests', () => {
  describe('Inter Management Detail Component', () => {
    let comp: InterDetailComponent;
    let fixture: ComponentFixture<InterDetailComponent>;
    const route = ({ data: of({ inter: new Inter(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FiberTestModule],
        declarations: [InterDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.inter).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
