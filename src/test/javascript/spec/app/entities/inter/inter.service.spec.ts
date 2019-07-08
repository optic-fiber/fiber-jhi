/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { InterService } from 'app/entities/inter/inter.service';
import { IInter, Inter, TypeInterEnum, ContractEnum } from 'app/shared/model/inter.model';

describe('Service Tests', () => {
  describe('Inter Service', () => {
    let injector: TestBed;
    let service: InterService;
    let httpMock: HttpTestingController;
    let elemDefault: IInter;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(InterService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Inter(0, 'AAAAAAA', TypeInterEnum.BAAP, ContractEnum.LM, 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateTimeInter: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Inter', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateTimeInter: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateTimeInter: currentDate
          },
          returnedFromService
        );
        service
          .create(new Inter(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Inter', async () => {
        const returnedFromService = Object.assign(
          {
            nd: 'BBBBBB',
            typeInter: 'BBBBBB',
            contract: 'BBBBBB',
            lastNameClient: 'BBBBBB',
            firstNameClient: 'BBBBBB',
            dateTimeInter: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateTimeInter: currentDate
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

      it('should return a list of Inter', async () => {
        const returnedFromService = Object.assign(
          {
            nd: 'BBBBBB',
            typeInter: 'BBBBBB',
            contract: 'BBBBBB',
            lastNameClient: 'BBBBBB',
            firstNameClient: 'BBBBBB',
            dateTimeInter: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateTimeInter: currentDate
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

      it('should delete a Inter', async () => {
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
