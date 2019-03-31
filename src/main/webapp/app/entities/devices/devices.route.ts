import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Devices } from 'app/shared/model/devices.model';
import { DevicesService } from './devices.service';
import { DevicesComponent } from './devices.component';
import { DevicesDetailComponent } from './devices-detail.component';
import { DevicesUpdateComponent } from './devices-update.component';
import { DevicesDeletePopupComponent } from './devices-delete-dialog.component';
import { IDevices } from 'app/shared/model/devices.model';

@Injectable({ providedIn: 'root' })
export class DevicesResolve implements Resolve<IDevices> {
    constructor(private service: DevicesService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDevices> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Devices>) => response.ok),
                map((devices: HttpResponse<Devices>) => devices.body)
            );
        }
        return of(new Devices());
    }
}

export const devicesRoute: Routes = [
    {
        path: '',
        component: DevicesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'robcoApp.devices.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DevicesDetailComponent,
        resolve: {
            devices: DevicesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'robcoApp.devices.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DevicesUpdateComponent,
        resolve: {
            devices: DevicesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'robcoApp.devices.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DevicesUpdateComponent,
        resolve: {
            devices: DevicesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'robcoApp.devices.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const devicesPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DevicesDeletePopupComponent,
        resolve: {
            devices: DevicesResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'robcoApp.devices.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
