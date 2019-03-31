import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IDevices } from 'app/shared/model/devices.model';
import { DevicesService } from './devices.service';

@Component({
    selector: 'jhi-devices-update',
    templateUrl: './devices-update.component.html'
})
export class DevicesUpdateComponent implements OnInit {
    devices: IDevices;
    isSaving: boolean;

    constructor(protected devicesService: DevicesService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ devices }) => {
            this.devices = devices;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.devices.id !== undefined) {
            this.subscribeToSaveResponse(this.devicesService.update(this.devices));
        } else {
            this.subscribeToSaveResponse(this.devicesService.create(this.devices));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevices>>) {
        result.subscribe((res: HttpResponse<IDevices>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
