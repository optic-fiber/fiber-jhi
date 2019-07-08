import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInter } from 'app/shared/model/inter.model';
import { InterService } from './inter.service';

@Component({
  selector: 'jhi-inter-delete-dialog',
  templateUrl: './inter-delete-dialog.component.html'
})
export class InterDeleteDialogComponent {
  inter: IInter;

  constructor(protected interService: InterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.interService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'interListModification',
        content: 'Deleted an inter'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-inter-delete-popup',
  template: ''
})
export class InterDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ inter }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(InterDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.inter = inter;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/inter', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/inter', { outlets: { popup: null } }]);
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
