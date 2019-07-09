import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IInter, Inter } from 'app/shared/model/inter.model';
import { InterService } from './inter.service';
import { IPlanning } from 'app/shared/model/planning.model';
import { PlanningService } from 'app/entities/planning';

@Component({
  selector: 'jhi-inter-update',
  templateUrl: './inter-update.component.html'
})
export class InterUpdateComponent implements OnInit {
  isSaving: boolean;

  plannings: IPlanning[];

  editForm = this.fb.group({
    id: [],
    nd: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(10)]],
    typeInter: [null, [Validators.required]],
    contract: [null, [Validators.required]],
    lastNameClient: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(100)]],
    firstNameClient: [null, [Validators.maxLength(100)]],
    dateTimeInter: [null, [Validators.required]],
    complex: [],
    planning: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected interService: InterService,
    protected planningService: PlanningService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ inter }) => {
      this.updateForm(inter);
    });
    this.planningService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlanning[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlanning[]>) => response.body)
      )
      .subscribe((res: IPlanning[]) => (this.plannings = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(inter: IInter) {
    this.editForm.patchValue({
      id: inter.id,
      nd: inter.nd,
      typeInter: inter.typeInter,
      contract: inter.contract,
      lastNameClient: inter.lastNameClient,
      firstNameClient: inter.firstNameClient,
      dateTimeInter: inter.dateTimeInter != null ? inter.dateTimeInter.format(DATE_TIME_FORMAT) : null,
      complex: inter.complex,
      planning: inter.planning
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const inter = this.createFromForm();
    if (inter.id !== undefined) {
      this.subscribeToSaveResponse(this.interService.update(inter));
    } else {
      this.subscribeToSaveResponse(this.interService.create(inter));
    }
  }

  private createFromForm(): IInter {
    return {
      ...new Inter(),
      id: this.editForm.get(['id']).value,
      nd: this.editForm.get(['nd']).value,
      typeInter: this.editForm.get(['typeInter']).value,
      contract: this.editForm.get(['contract']).value,
      lastNameClient: this.editForm.get(['lastNameClient']).value,
      firstNameClient: this.editForm.get(['firstNameClient']).value,
      dateTimeInter:
        this.editForm.get(['dateTimeInter']).value != null
          ? moment(this.editForm.get(['dateTimeInter']).value, DATE_TIME_FORMAT)
          : undefined,
      complex: this.editForm.get(['complex']).value,
      planning: this.editForm.get(['planning']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInter>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackPlanningById(index: number, item: IPlanning) {
    return item.id;
  }
}
