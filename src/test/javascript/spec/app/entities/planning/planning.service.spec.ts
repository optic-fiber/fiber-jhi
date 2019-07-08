/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PlanningService } from 'app/entities/planning/planning.service';
import { IPlanning, Planning } from 'app/shared/model/planning.model';

describe('Service Tests', () => {
  describe('Planning Service', () => {
    let injector: TestBed;
    let service: PlanningService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlanning;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PlanningService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Planning(0, 'AAAAAAA', false, currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateTimeCreation: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Planning', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateTimeCreation: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateTimeCreation: currentDate
          },
          returnedFromService
        );
        service
          .create(new Planning(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Planning', async () => {
        const returnedFromService = Object.assign(
          {
            initialTech: 'BBBBBB',
            open: true,
            dateTimeCreation: currentDate.format(DATE_TIME_FORMAT),
            lastNameTech: 'BBBBBB',
            firstNameTech: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTimeCreation: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Planning', async () => {
        const returnedFromService = Object.assign(
          {
            initialTech: 'BBBBBB',
            open: true,
            dateTimeCreation: currentDate.format(DATE_TIME_FORMAT),
            lastNameTech: 'BBBBBB',
            firstNameTech: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateTimeCreation: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Planning', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
