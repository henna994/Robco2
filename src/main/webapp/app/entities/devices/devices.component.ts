import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IDevices } from 'app/shared/model/devices.model';
import { AccountService } from 'app/core';
import { DevicesService } from './devices.service';

@Component({
    selector: 'jhi-devices',
    templateUrl: './devices.component.html'
})
export class DevicesComponent implements OnInit, OnDestroy {
    devices: IDevices[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected devicesService: DevicesService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.devicesService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IDevices[]>) => res.ok),
                    map((res: HttpResponse<IDevices[]>) => res.body)
                )
                .subscribe((res: IDevices[]) => (this.devices = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.devicesService
            .query()
            .pipe(
                filter((res: HttpResponse<IDevices[]>) => res.ok),
                map((res: HttpResponse<IDevices[]>) => res.body)
            )
            .subscribe(
                (res: IDevices[]) => {
                    this.devices = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInDevices();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IDevices) {
        return item.id;
    }

    registerChangeInDevices() {
        this.eventSubscriber = this.eventManager.subscribe('devicesListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
