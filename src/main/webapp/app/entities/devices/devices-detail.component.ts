import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDevices } from 'app/shared/model/devices.model';

@Component({
    selector: 'jhi-devices-detail',
    templateUrl: './devices-detail.component.html'
})
export class DevicesDetailComponent implements OnInit {
    devices: IDevices;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ devices }) => {
            this.devices = devices;
        });
    }

    previousState() {
        window.history.back();
    }
}
