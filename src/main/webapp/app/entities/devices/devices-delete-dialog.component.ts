import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDevices } from 'app/shared/model/devices.model';
import { DevicesService } from './devices.service';

@Component({
    selector: 'jhi-devices-delete-dialog',
    templateUrl: './devices-delete-dialog.component.html'
})
export class DevicesDeleteDialogComponent {
    devices: IDevices;

    constructor(protected devicesService: DevicesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.devicesService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'devicesListModification',
                content: 'Deleted an devices'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-devices-delete-popup',
    template: ''
})
export class DevicesDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ devices }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DevicesDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.devices = devices;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/devices', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/devices', { outlets: { popup: null } }]);
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
