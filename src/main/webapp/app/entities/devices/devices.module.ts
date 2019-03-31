import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RobcoSharedModule } from 'app/shared';
import {
    DevicesComponent,
    DevicesDetailComponent,
    DevicesUpdateComponent,
    DevicesDeletePopupComponent,
    DevicesDeleteDialogComponent,
    devicesRoute,
    devicesPopupRoute
} from './';

const ENTITY_STATES = [...devicesRoute, ...devicesPopupRoute];

@NgModule({
    imports: [RobcoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DevicesComponent,
        DevicesDetailComponent,
        DevicesUpdateComponent,
        DevicesDeleteDialogComponent,
        DevicesDeletePopupComponent
    ],
    entryComponents: [DevicesComponent, DevicesUpdateComponent, DevicesDeleteDialogComponent, DevicesDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RobcoDevicesModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
