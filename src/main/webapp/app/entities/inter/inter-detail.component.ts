import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInter } from 'app/shared/model/inter.model';

@Component({
  selector: 'jhi-inter-detail',
  templateUrl: './inter-detail.component.html'
})
export class InterDetailComponent implements OnInit {
  inter: IInter;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ inter }) => {
      this.inter = inter;
    });
  }

  previousState() {
    window.history.back();
  }
}
