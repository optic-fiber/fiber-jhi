import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanning } from 'app/shared/model/planning.model';
import { PlanningService } from './planning.service';

@Component({
  selector: 'jhi-planning-delete-dialog',
  templateUrl: './planning-delete-dialog.component.html'
})
export class PlanningDeleteDialogComponent {
  planning: IPlanning;

  constructor(protected planningService: PlanningService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.planningService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'planningListModification',
        content: 'Deleted an planning'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-planning-delete-popup',
  template: ''
})
export class PlanningDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ planning }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlanningDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.planning = planning;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/planning', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/planning', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
