import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IPlanning, Planning } from 'app/shared/model/planning.model';
import { PlanningService } from './planning.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-planning-update',
  templateUrl: './planning-update.component.html'
})
export class PlanningUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    initialTech: [null, [Validators.required, Validators.minLength(2)]],
    open: [null, [Validators.required]],
    dateTimeCreation: [null, [Validators.required]],
    lastNameTech: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(100)]],
    firstNameTech: [null, [Validators.maxLength(100)]],
    user: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected planningService: PlanningService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ planning }) => {
      this.updateForm(planning);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(planning: IPlanning) {
    this.editForm.patchValue({
      id: planning.id,
      initialTech: planning.initialTech,
      open: planning.open,
      dateTimeCreation: planning.dateTimeCreation != null ? planning.dateTimeCreation.format(DATE_TIME_FORMAT) : null,
      lastNameTech: planning.lastNameTech,
      firstNameTech: planning.firstNameTech,
      user: planning.user
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const planning = this.createFromForm();
    if (planning.id !== undefined) {
      this.subscribeToSaveResponse(this.planningService.update(planning));
    } else {
      this.subscribeToSaveResponse(this.planningService.create(planning));
    }
  }

  private createFromForm(): IPlanning {
    return {
      ...new Planning(),
      id: this.editForm.get(['id']).value,
      initialTech: this.editForm.get(['initialTech']).value,
      open: this.editForm.get(['open']).value,
      dateTimeCreation:
        this.editForm.get(['dateTimeCreation']).value != null
          ? moment(this.editForm.get(['dateTimeCreation']).value, DATE_TIME_FORMAT)
          : undefined,
      lastNameTech: this.editForm.get(['lastNameTech']).value,
      firstNameTech: this.editForm.get(['firstNameTech']).value,
      user: this.editForm.get(['user']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlanning>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }
}
